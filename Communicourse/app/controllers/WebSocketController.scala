package controllers

import java.net.URL
import javax.inject._

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.Materializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub, Source}
import play.api.mvc._
import service.ChatgroupService
import model.{User, UserForm}
import model.{Chatgroup, ChatgroupForm}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class WebSocketController @Inject()(implicit actorSystem: ActorSystem,
                               mat: Materializer,
                               executionContext: ExecutionContext)
  extends Controller {

  private type WSMessage = String
  
  private val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)

  private implicit val logging = Logging(actorSystem.eventStream, logger.getName)

  private val (chatSink, chatSource) = {

    val source = MergeHub.source[WSMessage]
      .log("source")
      .recoverWithRetries(-1, { case _: Exception ⇒ Source.empty })

    val sink = BroadcastHub.sink[WSMessage]
    source.toMat(sink)(Keep.both).run()
  }
  
  private val (chatSinka, chatSourcea) = {

    val source = MergeHub.source[WSMessage]
      .log("source")
      .recoverWithRetries(-1, { case _: Exception ⇒ Source.empty })

    val sink = BroadcastHub.sink[WSMessage]
    source.toMat(sink)(Keep.both).run()
  }
  
  private val userFlow: Flow[WSMessage, WSMessage, _] = {
     Flow.fromSinkAndSource(chatSink, chatSource).log("userFlow")
  }

  var userflow_list = List(userFlow)
  
  var index_flow = 0
  
  var userflow_create=false
  
  def index = Action.async { implicit request =>
    ChatgroupService.listAllChatgroups map { chatgroups =>
    if(! userflow_create){
      for(i <- 1 to chatgroups.last.idc.toInt){
          adduserflow
      }
      userflow_create=true
      }
    
       Ok(views.html.login(UserForm.form, Seq.empty[User]))
    
      }        
    }

  def adduserflow =  { 
      val (chatSinkc, chatSourcec) = {
        val source = MergeHub.source[WSMessage]
          .log("source")
          .recoverWithRetries(-1, { case _: Exception ⇒ Source.empty })
        val sink = BroadcastHub.sink[WSMessage]
          source.toMat(sink)(Keep.both).run()
      }  
   
        val new_userFlow: Flow[WSMessage, WSMessage, _] = {
          Flow.fromSinkAndSource(chatSinkc, chatSourcec).log("userFlow")
      }
      userflow_list=  userflow_list :+ new_userFlow  
  } 
  
  def socket(index:String): WebSocket = {
    WebSocket.acceptOrResult[WSMessage, WSMessage] {
      case rh if sameOriginCheck(rh) =>
        Future.successful(userflow_list.apply(index.toInt)).map { flow =>
          Right(flow)
        }.recover { 
          case e: Exception =>
            val msg = "Cannot create websocket"
            logger.error(msg, e)
            val result = InternalServerError(msg)
            Left(result)
        }

      case rejected =>
        logger.error(s"Request ${rejected} failed same origin check")
        Future.successful {
          Left(Forbidden("forbidden"))
        }
    }
  }
  
  private def sameOriginCheck(rh: RequestHeader): Boolean = {
    rh.headers.get("Origin") match {
      case Some(originValue) if originMatches(originValue) =>
        logger.debug(s"originCheck: originValue = $originValue")
        true

      case Some(badOrigin) =>
        logger.error(s"originCheck: rejecting request because Origin header value ${badOrigin} is not in the same origin")
        false

      case None =>
        logger.error("originCheck: rejecting request because no Origin header found")
        false
    }
  }

  private def originMatches(origin: String): Boolean = {
    try {
      val url = new URL(origin)
      url.getHost == "localhost" &&
        (url.getPort match { case 9000 | 19001  => true; case _ => false })
    } catch {
      case e: Exception => false
    }
  }

}
