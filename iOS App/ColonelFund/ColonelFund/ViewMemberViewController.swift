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
    
    
    var tempNameText: String = ""
    var tempUsernameText: String = "johnwsmith"
    var tempEmailText: String = "johnsmith@email.com"
    var tempPhoneText: String = "(555) 555-5555"
    
    //dummy array
    var eventList = [String]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        eventList.append("Independence Day BBQ")
        eventList.append("John Smith Chemo Fund")
        
        nameLabel.text = tempNameText
        usernameLabel.text = tempUsernameText
        emailLabel.text = tempEmailText
        phoneLabel.text = tempPhoneText
        
        associatedEventsTableView.delegate = self
        associatedEventsTableView.dataSource = self
        
        //TODO: - if no profile pic, use placeholder
        placeholderProfilePic()
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
        return eventList.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "AssociatedEventsTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? AssociatedEventsTableViewCell  else {
            fatalError("The dequeued cell is not an instance of AssociatedEventsTableViewCell.")
        }
        
        let event = eventList[indexPath.row]
        cell.eventNameLabel?.text = event
        
        return cell
    }
    
    func placeholderProfilePic() {
        let placeholder = UILabel()
        placeholder.frame.size = CGSize(width: 100.0, height: 100.0)
        placeholder.textColor = UIColor.white
        placeholder.font = UIFont.boldSystemFont(ofSize: 40)
        let name = nameLabel.text?.characters.split(separator: " ", maxSplits: 1).map(String.init)
        var firstName = name![0]
        var lastName = name![1]
        placeholder.text = String(describing: firstName.characters.first!) + String(describing: lastName.characters.first!)
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
            
            donateToMemberViewController.tempNameText = nameLabel.text!
            donateToMemberViewController.tempUsernameText = usernameLabel.text!
            
        case "ShowEvent":
            guard let eventViewController = segue.destination as? ViewEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedEventCell = sender as? AssociatedEventsTableViewCell else {
                fatalError("Unexpected sender: \(sender)")
            }
            
            guard let indexPath = associatedEventsTableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            let selectedEvent = eventList[indexPath.row]
            eventViewController.tempTitleText = selectedEvent
            
        default:
            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
        }
    }
    
    
}
