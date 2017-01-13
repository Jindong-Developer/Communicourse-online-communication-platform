package service

import model.{Chatgroup, Chatgroups}
import scala.concurrent.Future

object ChatgroupService {

  def addChatgroup(user: Chatgroup): Future[String] = {
    Chatgroups.add(user)
  }

  def deleteChatgroup(idc: Long): Future[Int] = {
    Chatgroups.delete(idc)
  }

  def getChatgroup(idc: Long): Future[Option[Chatgroup]] = {
    Chatgroups.get(idc)
  }


  def listAllChatgroups: Future[Seq[Chatgroup]] = {
    Chatgroups.listAll
  }
  
  def update_chatgroup(idc:String, members:String): Future[Int] = {
    Chatgroups.update_chatgroup(idc,members)
  }
  
}
