//
//  MainViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 10/14/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FacebookLogin
import Firebase
import FirebaseGoogleAuthUI
import GoogleSignIn


class MainViewController: UIViewController {
    //MARK: - Properties
    @IBAction func logoutButtonPressed(_ sender: Any) {
        print("You have been logged out")
        if let accessToken = FBSDKAccessToken.current(){
            LoginManager().logOut()
        }
        if GIDSignIn.sharedInstance().currentUser != nil {
            GIDSignIn.sharedInstance().signOut()
        }
        User.logout()
        performSegue(withIdentifier: "ShowLogin", sender: self)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        print(User.currentUser.getUserID())
        print(User.currentUser.getUserName())
        print(User.currentUser.getFirstName())
        print(User.currentUser.getLastName())
        print(User.currentUser.getEmailAddress())
        print(User.currentUser.getProfilePicURL())
        print(User.currentUser.getFacebookID())
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}
