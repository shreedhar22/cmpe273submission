package wallet
import javax.validation.constraints.NotNull
import com.github.nscala_time.time.Imports._
//import org.joda.time.format.DateTimeFormat

class User(){
   
   var userid:String=_;
   @NotNull
   var email:String=_;
   @NotNull
   var password:String=_;
   var name:String=_;
   var created_at:DateTime=_;   
   

    def getuserid():String={
      return this.userid;
   }
   
   def setuserid(counter:Int)={
     this.userid="u"+counter;
     
   }
   def getemail():String={
     return this.email;
   }
   def setemail(email:String){
     this.email=email;
   }
   
   def getpassword():String={
     return this.password;
   }
   def setpassword(password:String){
     this.password=password;
   }
   
   def getname():String={
     return this.name;
   }
   def setname(name:String){
     this.name=name;
   }

    def getcreated_at():String={
     created_at=DateTime.now;
     
     return created_at.toString;
    }

    
   
 }
