//
//  MyProfileViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FacebookLogin
import GoogleSignIn

class MyProfileViewController: UIViewController {
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var emailTextView: UITextView!
    @IBOutlet weak var phoneTextView: UITextView!
    @IBOutlet weak var profilePicImageView: UIImageView!
    
    
    var member: Member! = User.getCurrentUser()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        nameLabel.text = member.getFormattedFullName()
        usernameLabel.text = member.getUsername()
        emailTextView.text = member.getEmailAddress()
        print(member.getEmailAddress())
        phoneTextView.text = member.getPhoneNumber()
        
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
    }
    
    @IBAction func LogoutButtonPressed(_ sender: Any) {
        print("You have been logged out")
        if let accessToken = FBSDKAccessToken.current(){
            LoginManager().logOut()
        }
        GIDSignIn.sharedInstance().signOut()
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
