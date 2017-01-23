package service

import model.{Message, Messages}
import scala.concurrent.Future

object MessageService {

  def addMessage(message: Message): Future[String] = {
    Messages.add(message)
  }

  

  def getMessage(idm: Long): Future[Option[Message]] = {
    Messages.get(idm)
  }


  def listAllMessages: Future[Seq[Message]] = {
    Messages.listAll
  }
  
  
  
}