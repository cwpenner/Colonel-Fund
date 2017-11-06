//
//  ViewMemberViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class ViewMemberViewController: UIViewController {

    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    
    var tempNameText: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        nameLabel.text = tempNameText
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowDonateToMember":
            guard let donateToMemberViewController = segue.destination as? DonateToMemberViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            donateToMemberViewController.tempNameText = nameLabel.text!
            
        default:
            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
        }
    }
 

}
