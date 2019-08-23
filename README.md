# HUNT-Android

**Please follow the following structure**

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
    
2. to make a api request : we are using volley, we have some helper functions to help us with api calls
example : 
     
     val serviceInjector = ServiceVolley()
     
     val apiController = APIController(serviceInjector)
     
     apiController.post(path,params) // POST request :  params = JSONObject , path = String
     apiController.get(path,params) // GET request :  params =  String , path = String
     
     
 3. Rendring out the recyclerView adapters with **Groupie**: https://github.com/lisawray/groupie
 
**FIREBASE LOGIN:**
   username : hunt.recep.123@gmail.com
   password : Contact owner of this repo for password
   
   
**FACEBOOK LOGIN:**
   username : hunt.recep.123@gmail.com
   password : Contact owner of this repo for password

**For flow of app check out prototype :**
 
 Registration and setup profile
https://www.youtube.com/watch?v=dQoh8hdnhLU

Registration and setup profile
https://www.youtube.com/watch?v=dQoh8hdnhLU

social registration prototype
https://www.youtube.com/watch?v=o4FC1iQfTQo

map/location prototype 
https://www.youtube.com/watch?v=owGF0BklTDM

Swiping prototype-01
https://www.youtube.com/watch?v=Ffd_pN0jF2E

matching screen prototype
https://www.youtube.com/watch?v=6MOA48WCwoU

Profile setting Prototype
https://www.youtube.com/watch?v=zDvVX7KufF8

 filter process prototype
https://www.youtube.com/watch?v=zKYxXM6waXA&t=3s
\



