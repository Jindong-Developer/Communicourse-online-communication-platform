package service

import model.{User, Users}
import scala.concurrent.Future

object UserService {

  def addUser(user: User): Future[String] = {
    Users.add(user)
  }

  def deleteUser(id: Long): Future[Int] = {
    Users.delete(id)
  }
  
  def update_chatgroup(id: String,chatgroup:String): Future[Int] = {
    Users.update_chatgroup(id,chatgroup)
  }

  def getUser(id: Long): Future[Seq[User]] = {
    Users.get(id)
  }
  
  def checkUser(userName: String, password: String): Future[Seq[User]] = {
    Users.check(userName,password)
  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }
}
