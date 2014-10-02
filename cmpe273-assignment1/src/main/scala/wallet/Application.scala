package wallet

//import wallet.UserController
import org.springframework.boot.SpringApplication

object Application{

	def main(args: Array[String]){
		SpringApplication.run(classOf[UserController])	
	}	
}
