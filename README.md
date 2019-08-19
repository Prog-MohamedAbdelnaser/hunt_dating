# HUNT-Android

please follow the following structure 

class YourClassName:ParentClass{
    
    onCreate(){
      super....
      setContentView...
      
      init()
    }
    
    private fun init(){
      // initialize every view here , and other functions like setupViewPager(),setupRecyclerView
      //other funs call them here()       
    }
    
    //other funs 
}

2. to make a api request : we are using volley, we have some helper functions to help us with api calls
example : 
     
     val serviceInjector = ServiceVolley()
     val apiController = APIController(serviceInjector)
     
     apiController.post(path,params) // POST request :  params = JSONObject , path = String
     apiController.get(path,params) // GET request :  params =  String , path = String
     
     
 3. Rendring out the recyclerView adapters with Groupie: https://github.com/lisawray/groupie
     

