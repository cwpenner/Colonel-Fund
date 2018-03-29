//
//  MyActivityViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit

class MyActivityViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, EventCollectionProtocol {
    
    //EventCollectionProtocol
    //This has a EventCollection delegate reload the table when the data is finished being loaded
    func eventDataDownloaded() {
        member.setAssociatedEvents()
        associatedEventList = member.getAssociatedEvents()
        if self.refresher.isRefreshing
        {
            self.refresher.endRefreshing()
        }
        self.myEventsTableView.reloadData()
    }
    
    //MARK: Properties
    @IBOutlet var myDonationHistoryTableView: UITableView!
    @IBOutlet var myEventsTableView: UITableView!
    
    var member: Member! = User.getCurrentUser()
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
        EventCollection.sharedInstance.delegate = self
        member.setAssociatedEvents()
        associatedEventList = member.getAssociatedEvents()
        
        myDonationHistoryTableView.delegate = self
        myDonationHistoryTableView.dataSource = self
        
        myEventsTableView.delegate = self
        myEventsTableView.dataSource = self
        
        self.refresher = UIRefreshControl()
        self.refresher?.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refresher?.addTarget(self, action: #selector(self.refreshEventList(_:)), for: UIControlEvents.valueChanged)
        self.myEventsTableView?.addSubview(refresher!)
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
        var num = 1
        switch tableView {
        case myDonationHistoryTableView:
            num = 1
        case myEventsTableView:
            num = 1
        default:
            num = 1
        }
        return num
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var num = 1
        switch tableView {
        case myDonationHistoryTableView:
            num = 1
        case myEventsTableView:
            num = member.getAssociatedEvents().count
        default:
            num = 1
        }
        return num
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch tableView {
        case myDonationHistoryTableView:
            let cellIdentifier = "MyDonationHistoryTableViewCell"
            
            guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? MyDonationHistoryTableViewCell else {
                fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
            }
            
            //TODO: add info for donation cell here
            return cell
        case myEventsTableView:
            let cellIdentifier = "MyEventsTableViewCell"
            
            guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? MyEventsTableViewCell else {
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
        default:
            let cellIdentifier = "MyEventsTableViewCell"
            
            guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? MyEventsTableViewCell else {
                fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
            }
            return cell
        }
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
            
            guard let selectedEventCell = sender as? MyEventsTableViewCell else {
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = myEventsTableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            eventViewController.event = associatedEventList[indexPath.row]
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }
    

}
