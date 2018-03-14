Caravan App for Android. 

README:
2/18/18:
Compile and Run: 
1.	Download and install the latest version of Android Studio: 3.0.1. 
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

IN PROGRESS:

	- Invite users to party (by leader) [currently working on]
	- Join party based on Party Key (add follower) [currently working on]

TODO:

	- Change: User system, Invite system based on email addresses instead of usernames to increase potential userbase.
	- Journey Settings
	- Integrate Google Maps API in MainActivity (Create Party)
	- Revamp Create New Party Layout
	- Select start location (Geo LatLng)
	- Select destination (Geo LatLng)
	- Add back buttons instead of android default UI back button
	- Fix strange NullPointerOutofBounds exception on login
	- Add additional test cases
	- Tweak Verification methods (allow phone verification)
