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
        eventMemberTextView.text = member.getFormattedFullName()
    }
    
    //MARK: Properties
    @IBOutlet weak var eventImageView: UIImageView!
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateTextView: UITextView!
    @IBOutlet weak var eventTimeTextView: UITextView!
    @IBOutlet weak var eventAddressTextView: UITextView!
    @IBOutlet weak var eventTypeTextView: UITextView!
    @IBOutlet weak var eventMemberTextView: UITextView!
    @IBOutlet weak var eventFundGoalTextView: UITextView!
    @IBOutlet weak var eventCurrentFundsTextView: UITextView!
    @IBOutlet weak var eventDescriptionLabel: UILabel!
    @IBOutlet weak var eventProgressView: UIProgressView!
    
    var event: Event! = nil
    var member: Member! = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MemberCollection.sharedInstance.updateFromRemote()
        MemberCollection.sharedInstance.delegate = self
        
        member = MemberCollection.sharedInstance.getMember(userID: event.getAssociatedMember())
        eventMemberTextView.text = member.getFormattedFullName()

        eventTitleLabel.text = event.getTitle()
        eventDateTextView.text = event.getEventDate()
        eventTimeTextView.text = event.getEventTime()
        eventAddressTextView.text = event.getAddress().toString()
        eventTypeTextView.text = event.getEventType()
        eventFundGoalTextView.text = "$" + String(event.getFundGoal())
        eventCurrentFundsTextView.text = "$" + String(event.getCurrentFunds())
        eventDescriptionLabel.text = event.getEventDescription()
        print(event.getEventPicURL())
        if !event.getEventPicURL().isEmpty {
            loadImageFromURL(url: event.getEventPicURL())
        }
        
        var progress = Float(event.getCurrentFunds() / event.getFundGoal())
        if (progress < 0.5) {
            eventProgressView.progressTintColor = UIColor.red
        } else if (progress > 0.5 && progress < 1.0) {
            eventProgressView.progressTintColor = UIColor.yellow
        } else if (progress >= 1.0) {
            progress = 1.0
            eventProgressView.progressTintColor = UIColor.green
        }

        eventProgressView.setProgress(progress, animated: true)
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
            donateToEventViewController.tempMemberText = eventMemberTextView.text!
            
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
