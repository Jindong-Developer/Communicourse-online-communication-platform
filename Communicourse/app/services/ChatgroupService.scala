package service

import model.{Chatgroup, Chatgroups}
import scala.concurrent.Future

object ChatgroupService {

  def addChatgroup(chatgroup: Chatgroup): Future[String] = {
    Chatgroups.add(chatgroup)
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
  
  def update_chatgroup_members(idc:String, members:String): Future[Int] = {
    Chatgroups.update_chatgroup_members(idc,members)
  }
  
}
