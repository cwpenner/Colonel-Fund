//
//  LoginViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/21/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FacebookCore
import FacebookLogin
import FBSDKLoginKit
import Firebase
import FirebaseAuth
import FirebaseGoogleAuthUI
import GoogleSignIn




protocol LoginProtocol {
    func loginRequestComplete(loginMessage: String, loginSuccessful: Bool)
}

class LoginViewController: UIViewController, URLSessionDelegate, FBSDKLoginButtonDelegate, GIDSignInUIDelegate, GIDSignInDelegate, LoginProtocol {
    
    
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        if error != nil{
            print(error)
            return
        }
        print("Successfully logged in to Facebook FBSDK")
        guard let accessToken = FBSDKAccessToken.current() else {
            print("Failed to get access token")
            return
        }
        
        let credential = FacebookAuthProvider.credential(withAccessToken: accessToken.tokenString)
        
        
        
        Auth.auth().signIn(with: credential, completion:{(user, error) in
            if let error = error {
                print("Facebook authentication with Firebase error: ", error)
                return
            }
            print("User signed in with FB to Firebase!")
            self.getFBUserData()
            // Present main view
        })
    }
    
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        print("User Logged out of Facebook")
    }
    

    
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if (user != nil) {
            print(user.authentication)
            loginWithGoogle(authentication: user.authentication)
        }
    }
    
    
    //LoginProtocol
    //This has a LoginProtocal delegate update the UI after request is complete
    func loginRequestComplete(loginMessage: String, loginSuccessful: Bool) {
        self.loginMessageLabel.text = loginMessage
        self.loginMessageLabel.isHidden = false
        if loginSuccessful {
            self.performSegue(withIdentifier: "ShowMain", sender: self)
        }
    }

    let delegate: LoginProtocol! = nil
    let URL_FOR_LOGIN = "https://wesll.com/colonelfund/login.php"
    
    //MARK: - Properties
    @IBOutlet weak var usernameTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var fbLoginButton: FBSDKLoginButton!
    @IBOutlet weak var googleLoginButton: GIDSignInButton!
    
    
    @IBOutlet var loginMessageLabel: UILabel!
    
    
    var dict : [String : AnyObject]!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.loginMessageLabel.isHidden = true
        fbLoginButton.delegate = self
        fbLoginButton.readPermissions = ["email","public_profile"]

        GIDSignIn.sharedInstance().clientID = "955648583908-18fsgss07paoie7hs3f22g23vbude6n1.apps.googleusercontent.com"
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().delegate = self
     
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if Auth.auth().currentUser != nil{
            getFIRUserData()
        }
        //configureFBLoginButton()
        //isGoogleLoggedIn()
    }
   
    
    @IBAction func loginButtonPressed(_ sender: Any) {
        loginUser(emailAddress: usernameTextField.text!, password: passwordTextField.text!)
    }
    
    @IBAction func fbLoginButtonPressed(_ sender: Any) {

        print("FB LOGIN BUTTON PRESSED")
    }
    
    @IBAction func googleLoginButtonPressed(_ sender: Any){
        
        GIDSignIn.sharedInstance().signIn()
    }
    
   func configureFBLoginButton() {
    if((FBSDKAccessToken.current()) != nil){
        performSegue(withIdentifier: "ShowMain", sender: self)
        }
    }
    
    //Get user data
    func getFBUserData(){
        if((FBSDKAccessToken.current()) != nil){
            FBSDKGraphRequest(graphPath: "me", parameters: ["fields": "id, name, picture.type(large), email"]).start(completionHandler: { (connection, result, error) -> Void in
                if (error == nil){
                    self.dict = result as! [String : AnyObject]
                    print(result!)
                    print(self.dict)
                    let name = self.dict["name"] as! String
                    let nameSplit = name.split(separator: " ", maxSplits: 1).map(String.init)
                    let firstName = nameSplit[0]
                    let lastName = nameSplit[1]

                    let emailAddress = self.dict["email"] as! String  //FacebookSDK no longer returns email address
                    //Auth.auth().currentUser?.updateEmail(to: emailAddress)
                    print("FB EMAIL IS", Auth.auth().currentUser?.email)
                    let profilePic = self.dict["picture"] as! [String : AnyObject]
                    let profilePicData = profilePic["data"] as! [String : AnyObject]
                    let profilePicURL = profilePicData["url"] as! String
                    let facebookID = self.dict["id"] as! String
                    let member = Member(facebookID: facebookID, emailAddress: emailAddress, firstName: firstName, lastName: lastName, profilePicURL: profilePicURL)
                    User.setCurrentUser(currentUser: member)
                    self.performSegue(withIdentifier: "ShowMain", sender: self)
                }
            })
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func loginUser(emailAddress: String, password: String) {
        var loginSuccess = false
        var loginMessage = ""
        let url = URL(string: URL_FOR_LOGIN)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postBody = "emailAddress=\(emailAddress)&password=\(password)"
        request.httpBody = postBody.data(using: .utf8)
        let defaultSession = Foundation.URLSession(configuration: URLSessionConfiguration.default)
        var httpStatus = 0
        let task = defaultSession.dataTask(with: request) { (data, response, error) in
            let res = response as? HTTPURLResponse
            httpStatus = res!.statusCode
            if (error != nil) {
                loginMessage = "Error logging in. Error code: \(String(describing: error))"
                print(loginMessage)
                DispatchQueue.main.async(execute: { () -> Void in
                    self.loginRequestComplete(loginMessage: loginMessage, loginSuccessful: false)
                })
            } else {
                if (httpStatus == 200) {
                    loginMessage = "Connected to server successfully"
                    print(loginMessage)
                    loginSuccess = self.databaseLoginController(jsonData: data!)
                } else {
                    loginMessage = "Error connecting to server. Server response: \(httpStatus)"
                    print(loginMessage)
                    DispatchQueue.main.async(execute: { () -> Void in
                        self.loginRequestComplete(loginMessage: loginMessage, loginSuccessful: false)
                    })
                }
            }
        }
        task.resume()
    }
    
    func databaseLoginController(jsonData: Data) -> Bool {
        var loginMessage = ""
        let error: Bool
        do {
            let object = try JSONSerialization.jsonObject(with: jsonData, options: .allowFragments)
            print("LoginData: \(object)")
            if let dict = object as? [String: AnyObject] {
                error = dict["error"] as! Bool
                if (error) {
                    loginMessage = dict["error_msg"] as! String
                    DispatchQueue.main.async(execute: { () -> Void in
                        self.loginRequestComplete(loginMessage: loginMessage, loginSuccessful: false)
                    })
                } else {
                    let user = dict["user"] as? [String : AnyObject]
                    let member = try Member(json: user!)
                    User.setCurrentUser(currentUser: member)
                    loginMessage = "Successfully logged in as \(User.currentUser.getFormattedFullName())"
                    print(loginMessage)
                    DispatchQueue.main.async(execute: { () -> Void in
                        self.loginRequestComplete(loginMessage: loginMessage, loginSuccessful: true)
                    })
                    return true
                }
            }
        } catch {
            loginMessage = "Error! Unable to login"
            print(loginMessage)
            DispatchQueue.main.async(execute: { () -> Void in
                self.loginRequestComplete(loginMessage: loginMessage, loginSuccessful: false)
            })
        }
        return false
    }
    
    func isGoogleLoggedIn() {
        if GIDSignIn.sharedInstance().hasAuthInKeychain() == true {
            GIDSignIn.sharedInstance().signIn()
        }
    }
    
    func loginWithGoogle(authentication: GIDAuthentication){
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken, accessToken: authentication.accessToken)
        
        Auth.auth().signIn(with: credential, completion: {(user, error) in
            if error != nil{
                print(error!.localizedDescription)
                return
            }else {
                print(user?.email)
                print(user?.displayName)
                self.getFIRUserData()
            }
        })
    }
    func getFIRUserData(){
        let user = Auth.auth().currentUser
        let name = user?.displayName
        let nameSplit = name?.split(separator: " ", maxSplits: 1).map(String.init)
        let firstName = nameSplit?[0]
        let lastName = nameSplit?[1]
        let emailAddress = user?.email
        let profilePicURL = user?.photoURL?.absoluteString
        let firID = user?.uid
        // TEMPORARY
        if emailAddress == nil {
            print("EMPTY EMAIL")
            let member = Member(facebookID: firID!,emailAddress: emailAddress!, firstName: firstName!, lastName: lastName!, profilePicURL: profilePicURL!)
            User.setCurrentUser(currentUser: member)
        }
        else{
            let member = Member(googleID: firID!, emailAddress: emailAddress!, firstName: firstName!, lastName: lastName!, profilePicURL: profilePicURL!)
            User.setCurrentUser(currentUser: member)
        }

        self.performSegue(withIdentifier: "ShowMain", sender: self)
    }

    
    
    
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
//    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//        super.prepare(for: segue, sender: sender)
//        switch(segue.identifier ?? "") {
//        case "ShowMain":
//            guard let mainViewController = segue.destination as? MainViewController else {
//                fatalError("Unexpected destination: \(segue.destination)")
//            }
//
//            guard let loginButton = sender as? EventListTableViewCell else {
//                fatalError("Unexpected sender: \(sender)")
//            }
//
//
//        default:
//            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
//        }
//    }
 
    
}
