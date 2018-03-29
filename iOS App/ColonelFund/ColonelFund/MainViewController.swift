//
//  MainViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 10/14/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FacebookLogin
import Firebase
import FirebaseGoogleAuthUI
import GoogleSignIn


class MainViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, MemberCollectionProtocol, EventCollectionProtocol {
    
    //MemberCollectionProtocol
    //This has a MemberCollection delegate reload the table when the data is finished being loaded
    func memberDataDownloaded() {
        self.upcomingEventsTableView.reloadData()
        self.setTopContributors()
    }
    
    //EventCollectionProtocol
    //This has a EventCollection delegate reload the table when the data is finished being loaded
    func eventDataDownloaded() {
        if self.refresher.isRefreshing
        {
            self.refresher.endRefreshing()
        }
        self.upcomingEventsTableView.reloadData()
        self.setUpcomingEvents()
        print("UpcomingEvents count: \(upcomingEventsList.count)")
    }
    
    //MARK: - Properties
    @IBOutlet weak var upcomingEventsTableView: UITableView!
    @IBOutlet weak var topContributor1Label: UILabel!
    @IBOutlet weak var topContributor2Label: UILabel!
    @IBOutlet weak var topContributor3Label: UILabel!
    
    var upcomingEventsList = [Event]()
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
    var topContributor1: Member?
    var topContributor2: Member?
    var topContributor3: Member?
    var segueMem: Member?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        MemberCollection.sharedInstance.delegate = self
        EventCollection.sharedInstance.delegate = self
        
        EventCollection.sharedInstance.updateFromRemote()
        MemberCollection.sharedInstance.updateFromRemote()
        
        self.setUpcomingEvents()
        self.setTopContributors()
        
        upcomingEventsTableView.delegate = self
        upcomingEventsTableView.dataSource = self
        
        //Pull to Refresh
        self.refresher = UIRefreshControl()
        self.refresher?.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refresher?.addTarget(self, action: #selector(self.refreshEventList(_:)), for: UIControlEvents.valueChanged)
        self.upcomingEventsTableView?.addSubview(refresher!)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc private func refreshEventList(_ sender: Any) {
        EventCollection.sharedInstance.updateFromRemote()
        MemberCollection.sharedInstance.updateFromRemote()
    }
    
    //For displaying the 3 Events Ending Soon
    func setUpcomingEvents() {
        upcomingEventsList.removeAll()
        let temp = EventCollection.sharedInstance.eventArray
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let today = Date()
        //let calendar = Calendar.current
        //let futureDate = calendar.date(byAdding: .month, value: 1, to: Date())
        for event in temp {
            let eventDate = dateFormatter.date(from: event.getEventDate())
            if (eventDate! >= today && upcomingEventsList.count < 3) {
                upcomingEventsList.append(event)
                print("Event Title: \(event.getTitle())")
            }
        }
    }
    
    //For displaying the 3 Top Contributors
    func setTopContributors() {
        topContributor1 = MemberCollection.sharedInstance.getMember(userID: "666")
        topContributor2 = MemberCollection.sharedInstance.getMember(userID: "76194")
        topContributor3 = MemberCollection.sharedInstance.getMember(userID: "9351")
        
        topContributor1Label.text = topContributor1?.getFormattedFullName()
        topContributor2Label.text = topContributor2?.getFormattedFullName()
        topContributor3Label.text = topContributor3?.getFormattedFullName()
    }
    
    @IBAction func topContributor1ButtonPressed(_ sender: Any) {
        segueMem = topContributor1
        self.performSegue(withIdentifier: "ShowMember", sender: self)
    }
    
    @IBAction func topContributor2ButtonPressed(_ sender: Any) {
        segueMem = topContributor2
        self.performSegue(withIdentifier: "ShowMember", sender: self)
    }
    
    @IBAction func topContributor3ButtonPressed(_ sender: Any) {
        segueMem = topContributor3
        self.performSegue(withIdentifier: "ShowMember", sender: self)
    }
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return upcomingEventsList.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "UpcomingEventsTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? UpcomingEventsTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let event = upcomingEventsList[indexPath.row]
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
        
        let member = MemberCollection.sharedInstance.getMember(userID: event.getAssociatedMember())
        cell.memberLabel.text = member?.getFormattedFullName()
        
        
        return cell
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowEvent":
            guard let eventViewController = segue.destination as? ViewEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedEventCell = sender as? UpcomingEventsTableViewCell else {
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = upcomingEventsTableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            eventViewController.event = upcomingEventsList[indexPath.row]
            
        case "ShowMember":
            guard let contributorViewController = segue.destination as? ViewMemberViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            contributorViewController.member = segueMem
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }

}
