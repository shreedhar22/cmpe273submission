package wallet
import javax.validation.constraints.NotNull
class IdCard {
   var cardid:String="";
   @NotNull
   var card_name:String=_;
   @NotNull
   var card_number:String=_;
   var expiration_date:String="";
   
  def getcardid():String={
      return this.cardid;
   }
   
   def setcardid(counter:Int)={
     this.cardid=("i"+counter);
     
   } 
   
   def getcard_name():String={
     return this.card_name;
   }
   def setcard_name(card_name:String){
     this.card_name=card_name;
   }
   
   def getcard_number():String={
     return this.card_number;
   }
   def setcard_number(card_number:String){
     this.card_number=card_number;
   }
   
   def getexpiration_date():String={
     return this.expiration_date;
   }
   def setexpiration_date(expiration_date:String){
     this.expiration_date=expiration_date;
   }
   
}
