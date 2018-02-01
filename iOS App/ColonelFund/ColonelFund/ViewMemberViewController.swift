//
//  ViewMemberViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class ViewMemberViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var phoneLabel: UILabel!
    @IBOutlet weak var profilePicImageView: UIImageView!
    @IBOutlet weak var associatedEventsTableView: UITableView!
    
    var member: Member = Member(userID: "temp")
    let ec = EventCollection()
    var associatedEventList: [Event] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        member.setAssociatedEvents(eventList: ec.getEvents())
        associatedEventList = member.getAssociatedEvents()
        
        nameLabel.text = member.getFormattedFullName()
        usernameLabel.text = member.getUserName()
        emailLabel.text = member.getEmailAddress()
        phoneLabel.text = member.getPhoneNumber()
        
        associatedEventsTableView.delegate = self
        associatedEventsTableView.dataSource = self
        
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
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return member.getAssociatedEvents().count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "AssociatedEventsTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? AssociatedEventsTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let event = associatedEventList[indexPath.row]
        cell.eventNameLabel?.text = event.getTitle()
        
        return cell
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
    
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowDonateToMember":
            guard let donateToMemberViewController = segue.destination as? DonateToMemberViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            donateToMemberViewController.member = member
            
        case "ShowEvent":
            guard let eventViewController = segue.destination as? ViewEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedEventCell = sender as? AssociatedEventsTableViewCell else {
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = associatedEventsTableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            eventViewController.event = associatedEventList[indexPath.row]
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }
    
    
}
