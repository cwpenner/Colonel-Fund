//
//  EventListTableViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class EventListTableViewController: UITableViewController, EventCollectionProtocol, MemberCollectionProtocol {
    
    //MemberCollectionProtocol
    //This has a MemberCollection delegate reload the table when the data is finished being loaded
    func memberDataDownloaded() {
        self.eventListTableView.reloadData()
    }
    
    //EventCollectionProtocol
    //This has a EventCollection delegate reload the table when the data is finished being loaded
    func eventDataDownloaded() {
        eventList = ec.getEvents()
        if self.refresher.isRefreshing
        {
            self.refresher.endRefreshing()
        }
        self.eventListTableView.reloadData()
    }
    
    //MARK: Properties
    @IBOutlet var eventListTableView: UITableView!
    let ec = EventCollection()
    var eventList: [Event] = []
    let mc = MemberCollection()
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
        ec.delegate = self
        mc.delegate = self
        
        eventList = ec.getEvents()
        
        self.refresher = UIRefreshControl()
        self.refresher?.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refresher?.addTarget(self, action: #selector(self.refreshEventList(_:)), for: UIControlEvents.valueChanged)
        self.eventListTableView?.addSubview(refresher!)
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc private func refreshEventList(_ sender: Any) {
        self.ec.updateFromRemote()
    }
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return eventList.count
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "EventListTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? EventListTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let event = eventList[indexPath.row]
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
        
        let member = mc.getMember(userID: event.getAssociatedMember())
        cell.memberLabel.text = member?.getFormattedFullName()
        
        
        return cell
    }
    
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
    
    /*
     // Override to support editing the table view.
     override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
     if editingStyle == .delete {
     // Delete the row from the data source
     tableView.deleteRows(at: [indexPath], with: .fade)
     } else if editingStyle == .insert {
     // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
     }
     }
     */
    
    /*
     // Override to support rearranging the table view.
     override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {
     
     }
     */
    
    /*
     // Override to support conditional rearranging of the table view.
     override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the item to be re-orderable.
     return true
     }
     */
    
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowEvent":
            guard let eventViewController = segue.destination as? ViewEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedEventCell = sender as? EventListTableViewCell else {
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = tableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            eventViewController.event = eventList[indexPath.row]
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }
    
    
}

