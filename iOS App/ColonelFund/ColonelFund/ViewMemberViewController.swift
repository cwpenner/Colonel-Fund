//
//  ViewMemberViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class ViewMemberViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, EventCollectionProtocol {

    //EventCollectionProtocol
    //This has a EventCollection delegate reload the table when the data is finished being loaded
    func eventDataDownloaded() {
        member.setAssociatedEvents()
        associatedEventList = member.getAssociatedEvents()
        if self.refresher.isRefreshing
        {
            self.refresher.endRefreshing()
        }
        self.associatedEventsTableView.reloadData()
    }
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var emailTextView: UITextView!
    @IBOutlet weak var phoneTextView: UITextView!
    @IBOutlet weak var profilePicImageView: UIImageView!
    @IBOutlet weak var associatedEventsTableView: UITableView!
    
    var member: Member! = nil
    var associatedEventList: [Event] = []
    var refresher: UIRefreshControl!
    let months: [String] = ["J\nA\nN",
                            "F\nE\nB",
                            "M\nA\nR",
                            "A\nP\nR",
                            "M\nA\nY",
                            "J\nU\nN",
                            "J\nU\nL",
                            "A\nU\nG",
                            "S\nE\nP",
                            "O\nC\nT",
                            "N\nO\nV",
                            "D\nE\nC"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        EventCollection.sharedInstance.updateFromRemote()
        EventCollection.sharedInstance.delegate = self
        associatedEventList = member.getAssociatedEvents()
        
        nameLabel.text = member.getFormattedFullName()
        usernameLabel.text = member.getUserName()
        emailTextView.text = member.getEmailAddress()
        phoneTextView.text = member.getPhoneNumber()
        
        associatedEventsTableView.delegate = self
        associatedEventsTableView.dataSource = self
        
        if member.getProfilePicURL().isEmpty {
            placeholderProfilePic(member: member)
        } else {
            loadProfilePicFromURL(url: member.getProfilePicURL())
        }
        
        self.refresher = UIRefreshControl()
        self.refresher?.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refresher?.addTarget(self, action: #selector(self.refreshEventList(_:)), for: UIControlEvents.valueChanged)
        self.associatedEventsTableView?.addSubview(refresher!)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if let selectedRow: IndexPath = associatedEventsTableView.indexPathForSelectedRow {
            associatedEventsTableView.deselectRow(at: selectedRow, animated: animated)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc private func refreshEventList(_ sender: Any) {
        EventCollection.sharedInstance.updateFromRemote()
    }
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return member.getAssociatedEvents().count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "AssociatedEventsTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? AssociatedEventsTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let event = associatedEventList[indexPath.row]
        let eventType = event.getEventType().lowercased()
        let eventDate = event.getEventDate()
        let dayIndex = eventDate.index(eventDate.endIndex, offsetBy: -2)
        let dayString = String(eventDate[dayIndex...])
        let monthStartIndex = eventDate.index(eventDate.endIndex, offsetBy: -5)
        let monthEndIndex = eventDate.index(eventDate.endIndex, offsetBy: -3)
        let monthString = String(eventDate[monthStartIndex..<monthEndIndex])
        var progress = Float(event.getCurrentFunds() / event.getFundGoal())
        if (progress < 0.5) {
            cell.progressBar.progressTintColor = UIColor.red
        } else if (progress > 0.5 && progress < 1.0) {
            cell.progressBar.progressTintColor = UIColor.yellow
        } else if (progress >= 1.0) {
            progress = 1.0
            cell.progressBar.progressTintColor = UIColor.green
        }
        cell.nameLabel.text = event.getTitle()
        cell.dayLabel.text = String(Int(dayString)!)
        cell.monthLabel.text = months[Int(monthString)! - 1]
        cell.progressBar.setProgress(progress, animated: true)
        
        switch eventType {
        case "bbq":
            cell.eventIconImageView.image = UIImage(named: "bbq")
        case "emergency":
            cell.eventIconImageView.image = UIImage(named: "emergency")
        case "medical":
            cell.eventIconImageView.image = UIImage(named: "medical")
        case "party":
            cell.eventIconImageView.image = UIImage(named: "party")
        case "unknown":
            cell.eventIconImageView.image = UIImage(named: "unknown")
        default:
            cell.eventIconImageView.image = UIImage(named: "unknown")
        }
        
        cell.memberLabel.text = member?.getFormattedFullName()
        
        
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
    
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowDonateToMember":
            guard let donateToMemberViewController = segue.destination as? DonateToMemberViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            donateToMemberViewController.donateMember = member
            
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
