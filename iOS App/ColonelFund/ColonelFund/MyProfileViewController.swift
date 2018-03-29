//
//  MyProfileViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuthUI
import FirebaseGoogleAuthUI
import FirebaseFacebookAuthUI
import FBSDKCoreKit
import FacebookLogin
import GoogleSignIn



class MyProfileViewController: UIViewController,LoginProtocol {
    func loginRequestComplete(loginMessage: String, loginSuccessful: Bool) {
        self.loginMessageLabel.text = loginMessage
        self.loginMessageLabel.isHidden = false
        if loginSuccessful {
            print("LOGGED IN")
            self.viewDidAppear(true)
            //self.performSegue(withIdentifier: "ShowMyProfile", sender: self)
        }
    }
    
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    @IBOutlet weak var profilePicImageView: UIImageView!
    @IBOutlet weak var loginMessageLabel: UILabel!
    
    
    var member: Member! = User.getCurrentUser()
    let URL_FOR_LOGIN = "https://wesll.com/colonelfund/login.php"

    
    let delegate: LoginProtocol! = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.loginMessageLabel.isHidden = true
        nameLabel.text = member.getFormattedFullName()
        usernameLabel.text = member.getUserName()
        emailLabel.text = member.getEmailAddress()
        print(member.getEmailAddress())
        phoneLabel.text = member.getPhoneNumber()
        
        if member.getProfilePicURL().isEmpty {
            placeholderProfilePic(member: member)
        } else {
            loadProfilePicFromURL(url: member.getProfilePicURL())
        }
    }
    override func viewDidAppear(_ animated: Bool) {
        nameLabel.text = member.getFormattedFullName()
        usernameLabel.text = member.getUserName()
        emailLabel.text = User.getCurrentUser().getEmailAddress()// member.getEmailAddress()
        print("AFTER VIEWWILLAPPEAR", member.getEmailAddress())
        phoneLabel.text = member.getPhoneNumber()
        
        if member.getProfilePicURL().isEmpty {
            placeholderProfilePic(member: member)
        } else {
            loadProfilePicFromURL(url: member.getProfilePicURL())
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func placeholderProfilePic(member: Member) {
        let placeholder = UILabel()
        placeholder.frame.size = CGSize(width: 100.0, height: 100.0)
        placeholder.textColor = UIColor.white
        placeholder.font = UIFont.boldSystemFont(ofSize: 40)
        placeholder.text = String(describing: member.getFirstName().first!) + String(describing: member.getLastName().first!)
        placeholder.textAlignment = NSTextAlignment.center
        placeholder.backgroundColor = UIColor.darkGray
        placeholder.layer.cornerRadius = 50.0
        placeholder.layer.masksToBounds = true
        
        UIGraphicsBeginImageContext(placeholder.frame.size)
        placeholder.layer.render(in: UIGraphicsGetCurrentContext()!)
        profilePicImageView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    func loadProfilePicFromURL(url: String) {
        let imageURL = URL(string: url)
        do {
            let imageData = try Data(contentsOf: imageURL!)
            profilePicImageView.image = UIImage(data: imageData)
            profilePicImageView.layer.cornerRadius = 50.0
            profilePicImageView.layer.masksToBounds = true
        } catch {
            print("Error processing profile pic: \(error.localizedDescription)")
        }
    }
    
    @IBAction func LinkAccountButtonPressed(_ sender: Any) {
        //TODO: add Link Account code
        
        displayAlert(firebaseUser: Auth.auth().currentUser != nil)
    }
    
    func displayAlert(firebaseUser: Bool) {
        var usernameTextField: UITextField?
        var passwordTextField: UITextField?
        let alertTitle = "Link Account"
        let alertMessage = "Please enter your ColonelFund credentials below if you would like to link your ColonelFund account"
        let alertController = UIAlertController(title: alertTitle, message:
            alertMessage, preferredStyle: UIAlertControllerStyle.alert)
        
        if firebaseUser { //Use this loop if currently logged in with Firebase
            alertController.addTextField { (textField) -> Void in
                
                usernameTextField = textField
                usernameTextField?.placeholder = "ColonelFunds userID"
            }
            alertController.addTextField { (textField) -> Void in
                
                passwordTextField = textField
                passwordTextField?.isSecureTextEntry = true
            }
            alertController.addAction(UIAlertAction(title: "Login", style:  UIAlertActionStyle.default, handler: {action in //insert function here for what this button does}
                
                self.loginUser(emailAddress: usernameTextField!.text!, password: passwordTextField!.text!)
                
            }))
            alertController.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(alertController, animated: true, completion: nil)
            }
        else { //Use this loop if currently logged in with ColonelFund account and need to link Social accounts
        }
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
    @IBAction func LogoutButtonPressed(_ sender: Any) {
        print("You have been logged out")
        if let accessToken = FBSDKAccessToken.current(){
            LoginManager().logOut()
        }
        try! Auth.auth().signOut()
        performSegue(withIdentifier: "ShowLogin", sender: self)
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
