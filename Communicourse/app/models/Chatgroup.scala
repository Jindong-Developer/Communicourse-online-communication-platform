package model

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global



case class Chatgroup(idc: Long, group_name: String, members: String, owner: String, updated_time : String)

case class ChatgroupFormData(group_name: String,  owner: String,members: String )

object ChatgroupForm {

  val form = Form(
    mapping(
      
      "group_name" -> nonEmptyText,
      "owner" -> nonEmptyText,
      "members" -> nonEmptyText
      
    )(ChatgroupFormData.apply)(ChatgroupFormData.unapply)
  )
}

class ChatgroupTableDef(tag: Tag) extends Table[Chatgroup](tag, "chatgroup") {

  def idc = column[Long]("idc", O.PrimaryKey,O.AutoInc)
  def group_name = column[String]("group_name")
  def members = column[String]("members")
  def owner = column[String]("owner")
  def updated_time = column[String]("updated_time")


 override def * =
    (idc, group_name, members, owner, updated_time) <>(Chatgroup.tupled, Chatgroup.unapply)
}


object Chatgroups {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val chatgroups = TableQuery[ChatgroupTableDef]

  def add(chatgroup: Chatgroup): Future[String] = {
    dbConfig.db.run(chatgroups += chatgroup).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(idc: Long): Future[Int] = {
    dbConfig.db.run(chatgroups.filter(_.idc === idc).delete)
  }

  def get(idc: Long): Future[Option[Chatgroup]] = {
    dbConfig.db.run(chatgroups.filter(_.idc === idc).result.headOption)
  }
  
 

  def listAll: Future[Seq[Chatgroup]] = {
    dbConfig.db.run(chatgroups.result)
  }
  
  def listSome(idc:Long): Future[Seq[Chatgroup]] = {
    dbConfig.db.run(chatgroups.filter(_.idc === idc).result)
  }
  
  def update_chatgroup(idc:String, members:String): Future[Int] = {
 
    dbConfig.db.run(chatgroups.filter(_.idc === idc.toLong).map(p => (p.members))
        .update(members))
        
  }
  
 

}
