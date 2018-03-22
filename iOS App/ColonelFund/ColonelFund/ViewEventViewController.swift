//
//  ViewEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class ViewEventViewController: UIViewController, MemberCollectionProtocol {
    
    //MemberCollectionProtocol
    //This has a MemberCollection delegate reload the table when the data is finished being loaded
    func memberDataDownloaded() {
        let member = MemberCollection.sharedInstance.getMember(userID: event.getAssociatedMember())
        eventMemberLabel.text = member?.getFormattedFullName()
    }
    
    //MARK: Properties
    @IBOutlet weak var eventImageView: UIImageView!
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateLabel: UILabel!
    @IBOutlet weak var eventMemberLabel: UILabel!
    @IBOutlet weak var eventFundGoalLabel: UILabel!
    @IBOutlet weak var eventCurrentFundsLabel: UILabel!
    @IBOutlet weak var eventDescriptionLabel: UILabel!
    
    var event: Event! = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MemberCollection.sharedInstance.delegate = self

        eventTitleLabel.text = event.getTitle()
        eventDateLabel.text = event.getEventDate()
        eventFundGoalLabel.text = "$" + String(event.getFundGoal())
        eventCurrentFundsLabel.text = "$" + String(event.getCurrentFunds())
        eventDescriptionLabel.text = event.getEventDescription()
        
        if !event.getEventPicURL().isEmpty {
            loadImageFromURL(url: event.getEventPicURL())
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func loadImageFromURL(url: String) {
        let imageURL = URL(string: url)
        do {
            let imageData = try Data(contentsOf: imageURL!)
            eventImageView.image = UIImage(data: imageData)
        } catch {
            print("Error processing profile pic: \(error.localizedDescription)")
        }
    }
    

    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowDonateToEvent":
            guard let donateToEventViewController = segue.destination as? DonateToEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            donateToEventViewController.donateEvent = event
            donateToEventViewController.tempMemberText = eventMemberLabel.text!
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }

}
