# OAuth2 Examples


## Description
The project describes how Spring Oauth2 Clients work in details.

The following solutions implemented in the project:
  * **Social GitHub client** authorizes via GitHub provider;
  * **JSON Web Token** creates web token based on <a href="https://jwt.io/introduction/">JSON Web Token</a> library

### Social GitHub client

<a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/java/com/sme/oauth2/SocialGithubOAuth2Application.java">SocialGithubOAuth2Application</a> is simple application to authorize a request via OAuth2 GitHub client.

#### Configure New OAuth Application in GitHub
Do the following before starting **SocialGithubOauth2Application**:
 * log on to <a href="https://github.com/">github.com</a>
 * go to <a href="https://github.com/settings/developers">Settings -> Developers</a> and create New OAuth App
 * use "http://localhost:8080/" as Authorization callback URL
 * the application will create ClientID and ClientSecret
 * update **client-id** and **client-secrete** settings in <a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/resources/application-social.yml">application-social.yml</a>
 
#### Authorize with GitHub OAuth client  
Test **SocialGithubOauth2Application** by hand:
 * run **SocialGithubOauth2Application**
 * open <a href="http://localhost:8080/">localhost:8080</a> and press "Login with GitHub" link
 * the request will be redirected to <a href="https://github.com/login/oauth/authorize">github.com/login/oauth/authorize</a> page
 * accept authorization. GitHub will redirect the request to "http://localhost:8080/" (see Authorization callback URL)
 * open <a href="http://localhost:8080/user">localhost:8080/user</a> you will see all info about a registered user.
 
#### How it works

<a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/java/com/sme/oauth2/github/config/SocialGithubConfig.java#L49">SocialGithubConfig</a> configures authentication support using an OAuth 2.0.

When we press "Login with GitHub" link on <a href="http://localhost:8080/">localhost:8080</a>, Spring Security performs the following steps:
 * Spring Security calls OAuth2AuthorizationRequestRedirectFilter. The filter resolves GitHub provider with client-id and client-secret properties in <a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/resources/application-social.yml">application-social.yml</a>.
 * the request will be redirected to https://github.com/login/oauth/authorize?response_type=code&client_id=YOUR_CLIENT_ID&scope=read:user&state=STATE3D&redirect_uri=http://localhost:8080/login/oauth2/code/github login
 * when a user accepts authorization, GitHub calls a call back url with authorization code specified in OAuth App
 * DefaultOAuth2AuthorizationRequestResolver calls HttpSessionOAuth2AuthorizationRequestRepository and sets "HttpSessionOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST" with HashMap properties in the session
 * DefaultAuthorizationCodeTokenResponseClient sends POST request to get a token value: https://github.com/login/oauth/access_token,{grant_type=[authorization_code], code=[AUTH_CODE], redirect_uri=[http://localhost:8080/login/oauth2/code/github]. The request is sent with Basic Auth header
 * GitHub returns an answer with the following properties: expiresAt, scopes, token type, token value, etc
 * OAuth2LoginAuthenticationFilter loads user from https://api.github.com/user using token value
 * Now Spring Security has all info about GitHub principal that can be fetched by <a href="http://localhost:8080/user">localhost:8080/user</a> request.

### JSON Web Token

The implementation based on Spring Security and <a href="https://jwt.io/introduction/">JSON Web Token</a> library with some enhancements from <a href="https://www.youtube.com/watch?v=X80nJ5T7YpE">Java Brains</a>.


#### Important
The implementation uses stateless solution with REST controller.

But the solution is not recommended to be used in services:
* not scalable design;
* not shared (centralized) a secret token;
* etc.

#### Test by hand

Check <a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/java/com/sme/oauth2/jsonwebtoken/config/JsonWebTokenConfig.java">JsonWebTokenConfig</a> to check what pages need authentication.

Also check **jsonwebtoken** properties in <a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/resources/application-jsonwebtoken.yml">application-jsonwebtoken.yml</a>:
  * **users** is list of in-memory users
  * **secretKey** is a secret key to generate/fetch web token
  * **expirationInMs** is time to expire the token in milliseconds 

Run <a href="https://github.com/StepanMelnik/Oauth2_Examples/blob/master/src/main/java/com/sme/oauth2/JsonWebTokenApplication">JsonWebTokenApplication</a>.
The application will be started in Tomcat with 8080 port.

##### Get root page (not securable)

	GET / HTTP/1.1
	Host: localhost:8080
	Accept: text/html
	Content-Type: text/html
	Cache-Control: no-cache

	Response:
	200 code


##### Get /secure/hello page (securable)

	GET /secure/hello HTTP/1.1
	Host: localhost:8080
	Accept: text/html
	Content-Type: text/html
	Cache-Control: no-cache

	Response:
	403 code

##### Authorize a user

	POST /authenticate HTTP/1.1
	Host: localhost:8080
	Accept: application/json
	Content-Type: application/json
	Cache-Control: no-cache

	Body
	{
		"username": "admin",
		"password": "secret"
	}

	Response:
	{
	    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5NTc1NzY2MiwiaWF0IjoxNTk1NzU0MDYxfQ.5YnfpWIU5mkEL8kh76JGRtvBfZW8ETwF1r30fdLEem8"
	}

##### Get /secure/hello page (securable)

	GET /secure/hello HTTP/1.1
	Host: localhost:8080
	Accept: text/html
	Content-Type: text/html
	Authorization: Basic eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5NTc1NzY2MiwiaWF0IjoxNTk1NzU0MDYxfQ.5YnfpWIU5mkEL8kh76JGRtvBfZW8ETwF1r30fdLEem8
	Cache-Control: no-cache
	
	Response:
	Hello securable JsonWebToken

## Build

Clone and install <a href="https://github.com/StepanMelnik/Parent.git">Parent</a> project before building.

### Maven
	> mvn clean install
