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
        member.setAssociatedEvents(eventList: ec.getEvents())
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
    let ec = EventCollection()
    var associatedEventList: [Event] = []
    var refresher: UIRefreshControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ec.delegate = self
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
        self.ec.updateFromRemote()
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
            cell.eventNameLabel?.text = event.getTitle()
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
