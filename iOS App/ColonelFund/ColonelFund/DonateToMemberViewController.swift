//
//  DonateToMemberViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class DonateToMemberViewController: UIViewController {
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var memberDonationTextField: UITextField!
    @IBOutlet weak var profilePicImageView: UIImageView!
    
    var tempNameText: String = ""
    var tempUsernameText: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        nameLabel.text = tempNameText
        usernameLabel.text = tempUsernameText
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
