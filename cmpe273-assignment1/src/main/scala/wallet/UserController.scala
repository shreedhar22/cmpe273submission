package wallet
import scala.collection.mutable.ArrayBuffer
import Array._
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import scala.collection.mutable.MultiMap

import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
//import java.util.HashMap;
import org.springframework.web.bind.annotation._
import javax.validation.Valid                          // import this for validation
import org.springframework.validation.BindingResult 
import org.springframework.context.annotation.Configuration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan


//Conditional Get packages
import org.springframework.http._
import org.springframework.http.ResponseEntity;
import javax.ws.rs.core.CacheControl
import javax.ws.rs.core.EntityTag
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.github.nscala_time.time.Imports._;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@RestController
class UserController  {
 
         

       var  user=new User();
       var ucounter:Int=1;
	var icounter:Int=1;
	var wcounter:Int=1;
	var bcounter:Int=1;
	//var updated_at:DateTime=DateTime.now;
	var hm=new HashMap[String,User]();
        var hmID=new HashMap[String,Set[IdCard]]with MultiMap[String,IdCard];    // Stores IDcards 
        var hmWB=new HashMap[String,Set[WebLogin]]with MultiMap[String,WebLogin];  // Stores WebLogins
      var hmBA=new HashMap[String,Set[BankAccount]]with MultiMap[String,BankAccount];  // Stores BankAccounts
             
	@RequestMapping(value = Array("api/v1/user"), method = Array(RequestMethod.POST), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def CreateUser(@Valid @RequestBody user:User,result:BindingResult):User={
	  
	       if (result.hasErrors()) {
                  println("Post param missing")
               throw new MissingFieldError(result.toString)
           }
	       else{
		     this.user=user;
		     this.user.setuserid(ucounter);
                     var uid:String=this.user.getuserid();
		     this.hm+= (uid-> this.user);
		     var u:User = this.hm(uid);
		     println(u.getname());
		     ucounter=ucounter+1;
                     return user;
	       }
	}
	
	@RequestMapping(value=Array("api/v1/users/{userid}"), method=Array(RequestMethod.GET))
	def ViewUsers(@PathVariable("userid")  userId:String,@RequestHeader(value="If-None-Match", required=false) Etag: String):ResponseEntity[_]={
		var u:User =this.hm(userId);
                //u.getcreated_at();
                var tag: String = Etag
		var cc: CacheControl = new CacheControl()
        	cc.setMaxAge(216)
        	var etag: EntityTag = new EntityTag(Integer.toString(u.hashCode()));
              //   var up:String=updated_at.toString
               println(etag);
             //  println(up);
        	var responseHeader: HttpHeaders = new HttpHeaders	
        	responseHeader.setCacheControl(cc.toString())
        	responseHeader.add("Etag", etag.getValue())
        	if(etag.getValue().equalsIgnoreCase(tag)){
        		 println("Not_Modified");
                    new ResponseEntity[String]( null, responseHeader, HttpStatus.NOT_MODIFIED )   
        	} else {
                        println("Modified");
        		new ResponseEntity[User]( u, responseHeader, HttpStatus.OK )  
        	}
		
		//return u;
	} 

       @RequestMapping(value = Array("api/v1/users/{userid}"), method = Array(RequestMethod.PUT), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def UpdateUser( @PathVariable("userid") userId:String,@RequestBody user: User):User= {
	  
        //   println(user.getname());
	 // println(user.getemailid());
	 // println(user.getpassword());

       var u: User=this.hm(userId);
      if(user.getemail()==""){
        u.setemail(u.getemail());
         println("NULL");
      }
      else{
        u.setemail(user.getemail());
      }
      if(user.getpassword==""){
        u.setpassword(u.getpassword());
      }
      else{
        u.setpassword(user.getpassword());
      }
      this.hm-=(userId);
      this.hm+=(userId->u);
   //   println(this.hm(u.getuserid()).getemail());
   //    updated_at=DateTime.now + 2.months
    //  var up:String=updated_at.toString
      //         println(up);
	  return u;
     }

   



 /////////////IDCard///////////

	
  @RequestMapping(value = Array("api/v1/users/{userid}/idcards"), method = Array(RequestMethod.POST), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def CreateIdCard( @PathVariable("userid") userId:String, @Valid @RequestBody idcard:IdCard,result:BindingResult):IdCard={
		  if (result.hasErrors()) {
               throw new MissingFieldError(result.toString)
           }
	       else{  
          idcard.setcardid(icounter);
          icounter=icounter+1;
          this.hmID.addBinding(userId,idcard);   
         return idcard;
	}
      }
  
  
	@RequestMapping(value=Array("api/v1/users/{userid}/idcards"), method=Array(RequestMethod.GET), produces = Array("application/json"), headers=Array("content-type=application/json"))
	def ListAllIdCard(@PathVariable("userid")  userId:String ):Array[IdCard]={
		
	        
	        return hmID(userId).toArray;
     
	}
	
	@RequestMapping(value = Array("api/v1/users/{user_id}/idcards/{card_id}"), method = Array(RequestMethod.DELETE), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def DeleteIdCard( @PathVariable("user_id") userId:String,@PathVariable("card_id") card_id:String):Array[IdCard]={
	  
	  var a= new Array[IdCard](10);
	  a=hmID(userId).toArray;
	  var i:Int=0;
	    while(a(i).getcardid()!=card_id){
	      i=i+1;
	    }
	  var idcard:IdCard=a(i);
	  hmID.removeBinding(userId, idcard);
	  return hmID(userId).toArray;
	}


	////////////WebLogin///////////////
	
	@RequestMapping(value = Array(" api/v1/users/{user_id}/weblogins"), method = Array(RequestMethod.POST), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def CreateWebLogin( @PathVariable("user_id") userId:String, @Valid @RequestBody weblogin:WebLogin,result:BindingResult):WebLogin={    
        if (result.hasErrors()) {
               throw new MissingFieldError(result.toString)
           }
	       else{        
  
       weblogin.setloginid(wcounter);
       wcounter=wcounter+1;
	     this.hmWB.addBinding(userId,weblogin);   
         return weblogin;
	}
     }
  
    @RequestMapping(value=Array("api/v1/users/{user_id}/weblogins"), method=Array(RequestMethod.GET), produces = Array("application/json"), headers=Array("content-type=application/json"))
	def ListAllWebLogins(@PathVariable("user_id")  userId:String ):Array[WebLogin]={
		
	        
	        return hmWB(userId).toArray;
     
	}
	
    @RequestMapping(value = Array("api/v1/users/{user_id}/weblogins/{login_id}"), method = Array(RequestMethod.DELETE), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def DeleteWebLogin( @PathVariable("user_id") userId:String,@PathVariable("login_id") login_id:String):Array[WebLogin]={
	  
	  var a= new Array[WebLogin](10);
	  a=hmWB(userId).toArray;
	  var i:Int=0;
	    while(a(i).getloginid()!=login_id){
	      i=i+1;
	    }
	  var weblogin:WebLogin=a(i);
	  hmWB.removeBinding(userId, weblogin);
	  return hmWB(userId).toArray;
	}
  
  ////////////BankAccount/////////////////
  
  @RequestMapping(value = Array(" api/v1/users/{user_id}/bankaccounts"), method = Array(RequestMethod.POST), consumes = Array("application/json"),headers=Array("content-type=application/json"))
	@ResponseBody
	def CreateBankAccount( @PathVariable("user_id") userId:String, @Valid @RequestBody bankaccount:BankAccount,result:BindingResult):BankAccount={    
     
       if (result.hasErrors()) {
               throw new MissingFieldError(result.toString)
           }
	       else{   
      bankaccount.setba_id(bcounter);
            bcounter=bcounter+1;
	     this.hmBA.addBinding(userId,bankaccount);   
         return bankaccount;
	}
  }

    @RequestMapping(value=Array("api/v1/users/{user_id}/bankaccounts"), method=Array(RequestMethod.GET), produces = Array("application/json"), headers=Array("content-type=application/json"))
	def ListAllBankAccounts(@PathVariable("user_id")  userId:String ):Array[BankAccount]={
		
	        
	        return hmBA(userId).toArray;
     
	}
  
  
    @RequestMapping(value = Array("api/v1/users/{user_id}/bankaccounts/{bank_id}"), method = Array(RequestMethod.DELETE), consumes = Array("application/json"),
            headers=Array("content-type=application/json"))
	@ResponseBody
	def DeleteBankAccount( @PathVariable("user_id") userId:String,@PathVariable("bank_id") bank_id:String):Array[BankAccount]={
	  
	  var a= new Array[BankAccount](10);
	  a=hmBA(userId).toArray;
	  var i:Int=0;
	    while(a(i).getba_id()!=bank_id){
	      i=i+1;
	    }
	  var bankaccount:BankAccount=a(i);
	  hmBA.removeBinding(userId, bankaccount);
	  return hmBA(userId).toArray;
	}

}
