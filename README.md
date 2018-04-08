Caravan App for Android. 

README:
2/18/18:
Compile and Run: 
1.	Download and install Android Studio 3.0.1: https://dl.google.com/dl/android/studio/install/3.0.1.0/android-studio-ide-171.4443003-windows.exe 
2.	Choose the import project option form the main menu. 
3.	Select the cen-project directory within the cen4020 directory and click open.
4.	Follow direction provided by android studio to install any necessary libraries. 
5.	In order to emulate the app, you must select an Android virtual device. For development purposes, choose the Nexus 5 with the Lollipop flavor of Android. 
6.	Once completed, press the play button in the top right-hand corner and select the Nexus 5.
7.	Open the app, and follow the onscreen instructions

Unit Testing:

1.	Within Android Studio, navigate to:
	cen-project/app/src/androidTest/java/edu.fsu.cen4020.cen_project/LoginActivityTest

2.	Choose between individual test cases or all test cases
3.	To run test cases:	
	a.	Select an individual unit test by pressing the play button located next to the line number. 
	b.	Press the play button in the top right corner of Android Studio to run all test cases. 
Acceptance Tests:
	a.	LoginActivityTest:  Input: Valid Email, Output: Boolean 
			Sample: Email: cen4020proj@fsu.com, Output: True
	        Email: bademail#uf.gators, Output: False


Status:
	
	- Integrated Firebase API (Database and Verification) [check]
	- Login, Register activity skeletons and layouts added [check]
	- Test case skeletons to be implemented [check]
	- Add Logout Button and functionality to main screen [check]
	- View my (logged in user’s) parties via Spinner Drop Down [check]
	- Select party from dropdown to view followers [check]
	- Load followers from selected party into ListView Box for monitoring [check]
	- Fix layout for Journey Activity (activity_journey.xml) [check]
	- Invite users to party (by leader) [check]
	- Join existing party by PartyKey:Password (add follower to party) [check]
	- List party's that user 'follows', and 'my parties' [check]
	- Integrate Google Maps API in MainActivity (Create Party) [check]
	- Revamp Create New Party Layout [check]
	- Added launch support for Journey Paries [check]
	- Select start location (Geo LatLng--Google Maps API, Google Places API) [check]
	- Select destination (Geo LatLng--Google Maps API, Google Places API) [check]
	- Google Places API integrated for location and map monitoring on Google Maps interface [check]
	- Multi-user support [check]
	- Journey Lobby for followers integrated: allows followers to enter journey that has been launched (via listener confirmation) [check]
	- UI and Logo Graphic [check]
	- Location updates for leader and follower on Launched Journey map [check]
	- Location for stop and start location added on Launched Journey map [check]


IN PROGRESS:

	- Add User->Inbox functionality to respond to invites [todo]

TODO:

	- Potential Change: User system, Invite system based on email addresses instead of usernames to increase potential userbase.
	- Additional launch functionality: broadcast sender and receiver for journey launch
	- Text message support to send PartyKey:Password to a user
	- Add back buttons instead of android default UI back button
	- Fix strange NullPointerOutofBounds exception on login
	- Add additional test cases
	- Tweak Verification methods (allow phone verification)
