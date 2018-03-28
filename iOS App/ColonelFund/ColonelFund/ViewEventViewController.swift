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
    //This has a MemberCollection delegate reload the view when the data is finished being loaded
    func memberDataDownloaded() {
        member = MemberCollection.sharedInstance.getMember(userID: event.getAssociatedMember())
        eventMemberLabel.text = member.getFormattedFullName()
    }
    
    //MARK: Properties
    @IBOutlet weak var eventImageView: UIImageView!
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateLabel: UILabel!
    @IBOutlet weak var eventTimeLabel: UILabel!
    @IBOutlet weak var eventAddressLabel: UILabel!
    @IBOutlet weak var eventTypeLabel: UILabel!
    @IBOutlet weak var eventMemberLabel: UILabel!
    @IBOutlet weak var eventFundGoalLabel: UILabel!
    @IBOutlet weak var eventCurrentFundsLabel: UILabel!
    @IBOutlet weak var eventDescriptionLabel: UILabel!
    
    var event: Event! = nil
    var member: Member! = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MemberCollection.sharedInstance.updateFromRemote()
        MemberCollection.sharedInstance.delegate = self
        
        member = MemberCollection.sharedInstance.getMember(userID: event.getAssociatedMember())
        eventMemberLabel.text = member.getFormattedFullName()

        eventTitleLabel.text = event.getTitle()
        eventDateLabel.text = event.getEventDate()
        eventTimeLabel.text = event.getEventTime()
        eventAddressLabel.text = event.getAddress().toString()
        eventTypeLabel.text = event.getEventType()
        eventFundGoalLabel.text = "$" + String(event.getFundGoal())
        eventCurrentFundsLabel.text = "$" + String(event.getCurrentFunds())
        eventDescriptionLabel.text = event.getEventDescription()
        print(event.getEventPicURL())
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
            print("Error processing Event pic: \(error.localizedDescription)")
            eventImageView.image = UIImage(named: "img_placeholder")
            
        }
    }
    
    @IBAction func memberDetailButtonPressed(_ sender: Any) {
        performSegue(withIdentifier: "ShowMember", sender: sender)
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
            
        case "ShowMember":
            guard let memberViewController = segue.destination as? ViewMemberViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            memberViewController.member = member
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }

}
