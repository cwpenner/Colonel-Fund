//
//  MyActivityViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit

class MyActivityViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    //MARK: Properties
    @IBOutlet var myDonationHistoryTableView: UITableView!
    @IBOutlet var myEventsTableView: UITableView!
    
    //dummy array
    var eventList = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        eventList.append("Independence Day BBQ")
        eventList.append("John Smith Chemo Fund")
        
        myDonationHistoryTableView.delegate = self
        myDonationHistoryTableView.dataSource = self
        
        myEventsTableView.delegate = self
        myEventsTableView.dataSource = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
            num = eventList.count
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
            
            let event = eventList[indexPath.row]
            cell.eventNameLabel?.text = event
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
                fatalError("Unexpected sender: \(sender)")
            }
            
            guard let indexPath = myEventsTableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            let selectedEvent = eventList[indexPath.row]
            eventViewController.tempTitleText = selectedEvent
            
        default:
            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
        }
    }
    

}
