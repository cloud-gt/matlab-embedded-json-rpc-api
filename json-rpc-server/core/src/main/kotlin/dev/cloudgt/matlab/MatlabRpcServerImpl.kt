package dev.cloudgt.matlab

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Request.Companion.invoke
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings
import org.http4k.jsonrpc.JsonRpc
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.server.Http4kServer
import org.http4k.server.websocket.JavaWebSocket
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse
import java.util.concurrent.CompletableFuture.runAsync

class MatlabRpcServerImpl(private val engineFactory: () -> MatlabEngineApi) : MatlabRpcServer {

    private lateinit var server: Http4kServer

    private val ws: RoutingWsHandler

    init {
        ws = websockets(
            "/" bind { req: Request ->
                WsResponse { ws: Websocket ->

                    val engine = engineFactory()
                    ws.onClose { engine.disconnect() }

                    ws.send(WsMessage(""" {"jsonrpc": "2.0", "method": "connected", "params": {"message" : "Successfully connected to MATLAB."}} """))

                    val matlab = Matlab(
                        matlabEngine = engine,
                        WebSocketWriter(ws, "stdout"),
                        WebSocketWriter(ws, "stderr")
                    )

                    val rpcHandler: HttpHandler = createHandler(matlab)

                    ws.onMessage { m ->
                        runAsync {
                            val response = rpcHandler(
                                Request(POST, "/rpc")
                                    .header("Content-Type", "application/json")
                                    .body(m.body)
                            )

                            ws.send(WsMessage(response.body))
                        }
                    }
                }
            }
        )
    }

    override fun start(port: Int) {
        server = JavaWebSocket(port = port)
            .toWsServer(ws)
            .start()
    }

    override fun stop() {
        server.stop()
    }

    override fun port(): Int {
        return server.port()
    }

    private fun createHandler(matlab: Matlab): HttpHandler = JsonRpc.auto(CustomJackson, MatlabErrorHandler) {
        method("eval", handler(matlab::eval))
        method("feval", handler(matlab::feval))
        method("getVariable", handler(matlab::getVariable))
        method("putVariable", handler(matlab::putVariable))
    }

    private object CustomJackson : ConfigurableJackson(
        KotlinModule.Builder().build()
            .asConfigurable()
            .withStandardMappings()
            .done()
            .deactivateDefaultTyping()
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false)
            .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false)
    )

}
