<html>
      <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="style.css" rel="stylesheet" type="text/css" />
    </head>

    <body>
        
        <div id="f1">
        <h1>This is my Photo Gallery, make your own...</h1>
        </div>
            <p>Please enter your password and username</p></h1>
        <div id="user">
        <form action="login" method="post">
            <label>Username</label> <input type="text" name="username"> <br>
            <label>Password</label> <input type="password" name="password"> <br>
            <input type="submit">
        </form>
        </div>
        <a href="register.html">Register</a><br><br>
        <div id ="per">
            <h2> Choose the links below in order to see:</h2>
        <a href="statistics?display=login">Users Online</a><br> 
        <a href="statistics?display=register">Users Registered</a> <br>
        <a href="statistics?display=photos">Photos Uploaded</a><br>
        </div>
        
        <ul class="gallery">
						<li>
				<img src="pictures/woman.jpg"  >
				<p><a href="#">Woman</a></p>
			</li>
			<li>
				<img src="pictures/sunset.jpg"  />
				<p><a href="#">Sunset..</a></p>
			</li>
			<li>
				<img src="pictures/car.jpg"  />
				<p><a href="#">Dream Car</a></p>
			</li>
			<li>
				<img src="pictures/cat.jpg" />
				<p><a href="#">Miaouuu</a></p>
			</li>
			<li>
				<img src="pictures/sunset.jpg"/>
				<p><a href="#">Sunset</a></p>
			</li>
			<li>
				<img src="pictures/breakfast.jpg"/>
				<p><a href="#">Breakfast Time</a></p>
			</li>
			<li>
				<img src="pictures/boots.jpg" />
				<p><a href="#">Boots</a></p>
			</li>
			<li>
				<img src="pictures/boat.jpg" />
				<p><a href="#">Boat</a></p>
			</li>
                        <li>
				<img src="pictures/Penguins.jpg"  >
				<p><a href="#">Penguins</a></p>
			</li>
		</ul>
	
       
    </body>
</html>
