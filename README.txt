This is simple Android test App that reads Ascii characters from online server into
the android app, and sort then in the nice layour view with related info.
This app can be further developed and code implented to server as a news reader
or any other type of online - networking app. See the SCREENSHOT for the idea behind the app.
This code and app serves for demo and testing purposes.


Here is the interesting Android Task that I have got as a interview question in one occasion. It is a good practice for Android starters, and I have included code here so you can test it, improve or do whatever you want with it. I had limited amount of time to finish, so this code can be further improved to be more reflective and efficient. 

TASK: ---------------------------------------------- 

For the android exam you'll need to create a very simple app, called "Discount Ascii Warehouse". You can get search results from the API with a request like this: > curl http://74.50.59.155:5000/api/search The API also accepts some parameters: 
GET /api/search 
Parameters limit (int)  - Max number of search results to return 
skip (int) - Number of results to skip before sending back search results 
q (string) - Search query. Tags separated by spaces. 
onlyInStock (bool) - When flag is set, only return products that are currently in stock. 

Response Type: NDJSON The app should keep loading products from the API until it has enough to fill the screen, and then wait until the user has swiped to the bottom to load more. 
The app should cache API requests for 1 hour. 

---------------------------------------- ANSWER: ---------------------------------------- 

First of all here is the link where you can download zipped project code, compiled apk, few screenshots, and notes copied from this email: https://github.com/nikolakrivokapic/ascii_faces_simple_android_test After few days of work I have done the task and following the link below is the full project from Android Studio and compiled .apk file ready to run on any Android device or emulator such is Virtual Box ( https://www.virtualbox.org/ ) . 

You can build the project from extracted project root directory command prompt with the command "gradlew.bat build" (Windows) or "./gradlew build" (Linux). For this to work you need to have JAVA SDK installed and configured on your machine. After that you can find compiled apk inside /apk/build/outputs/apk/ directory . Then you can import this apk into any Android Device or VM such is Virtual Box. It would be ideal to open the project in Android Studio and build it and test it, but if you prefer to run it on Virtual Box, you need to install Android system - download latest Android ISO from www.android-x86.org and make new virtual machine inside Virtual Box, with this ISO. 

Then there should be the option to install the apk inside this virtual machine. Regarding technical part of developing. From testing point of view, all functionalities of my app work as you described. Let me say that after some initial testing I realized that onlyInStock parameter doesn't functioning with the API, so if I send request with this flag set, i am still getting the lines where stock=0 . This I tested many times, and didn't succeeded - I tried to set both onlyInStock=true and onlyInStock=1 . 

This complicated some things, but anyway, i solved this problem programatically inside the code, to separate those products which are in stock and those which are not. The app has one activity (MainActivity) and GridView element is initialized using CustomAdapter class which extends BaseAdapter base class. GridView is updated as needed based on search criteria. As the app is using many HTTP requests, I utilized this through the AsyncTasks and separating the app in different segments/methods, calling them -as needed. On the GridView there is set on-scroll listener which loads more products as the user scrolls down the list. I organised the grid as a list of 3x3 columns/rows. So this can be adapted and tweaked in the future as needed to make different layout. After every HTTP request I am calling mListAdapter.notifyDataSetChanged(); to notify GridView that data is updated inside the lists of strings that are chained with this GridView, so GridView is adding new items based on the search criteria. 

I used typical JSON objects making them from line separated values (product data). Inside the HTTP request AsyncTask I used very simple and useful HttpURLConnection library so this allowed me to utilize HttpResponseCache library for caching data for one hour. I have try/catch condition , where app is first trying to load data from cache and if data is not found, then load it from online source. App will give the message when loading the data and inform of the data source (cache or online source). Alternating color on the products where only 1 product left in stock I have done also with the AsyncTask inside CustomAdapter class, as the AsyncTask is working great to make blinking fields work separately from each other. 

If this were going to be a long term project, there could be many directions for improvement. First of all we should check if I am right that onlyInStock flag is not working in the API, so if this is fixed the app itself could be re-coded with simpler logic. I always tend to use latest libraries and light technology choices when it comes to developing apps such is this one. So this should be one direction, to continuously check if the app can be made with the more elegant logic, latest functionalities and libraries to be implemented, but always making sure that it is going to be supported and run on as many possible devices. Graphical layout can be definitely much elegant and eye-catching for the user, as this one now is very simple and basic form. UI in general can see a lot of improvements here. If team were going to work on this project, one member of the team could take care about graphical appearance of the app, present new ideas, solutions or work on graphical improvement and layouts. Other team member can be involved in sole testing of the app and giving the possible solutions/recommendations, presenting possible bugs and problems in app running circle. And of course one member should work on back-end part of the app and logic behind it, and he should be a project leader as he hold the "key" of the app. All team should discuss the possible improvements and directions of development.
