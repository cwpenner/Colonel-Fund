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

class MyProfileViewController: UIViewController {
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    @IBOutlet weak var profilePicImageView: UIImageView!
    
    
    var member: Member! = User.getCurrentUser()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        nameLabel.text = member.getFormattedFullName()
        usernameLabel.text = member.getUserName()
        emailLabel.text = member.getEmailAddress()
        phoneLabel.text = member.getPhoneNumber()
        
        if member.getProfilePicURL().isEmpty {
            placeholderProfilePic(member: member)
        } else {
            //TODO: display profile pic from URL
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
    
    @IBAction func LinkAccountButtonPressed(_ sender: Any) {
        //TODO: add Link Account code
    }
    
    @IBAction func LogoutButtonPressed(_ sender: Any) {
        print("You have been logged out")
        //TODO: perform logout activities
        if let accessToken = FBSDKAccessToken.current(){
            LoginManager().logOut()
        }
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
