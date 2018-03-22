//
//  CreateEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class CreateEventViewController: UIViewController, UITextViewDelegate, UITextFieldDelegate {

    //MARK: Properties
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var createEventImage: UIImageView!
    @IBOutlet weak var createEventTitleTextField: UITextField!
    @IBOutlet weak var createEventDateTextField: UITextField!
    @IBOutlet weak var createEventTimeTextField: UITextField!
    @IBOutlet weak var createEventAddressTextView: UITextView!
    @IBOutlet weak var createEventTypeTextField: UITextField!
    @IBOutlet weak var createEventMemberTextField: UITextField!
    @IBOutlet weak var createEventFundGoalTextField: UITextField!
    @IBOutlet weak var createEventDescriptionTextView: UITextView!
    
    var event: Event! = nil
    let URL_FOR_CREATE_EVENT = "https://wesll.com/colonelfund/create_event.php"
    var activeView: UITextView?
    var activeField: UITextField?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        createEventTitleTextField.delegate = self
        createEventDateTextField.delegate = self
        createEventTimeTextField.delegate = self
        createEventTypeTextField.delegate = self
        createEventMemberTextField.delegate = self
        createEventFundGoalTextField.delegate = self
        createEventAddressTextView.delegate = self
        createEventDescriptionTextView.delegate = self
        
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self.view, action: #selector(UIView.endEditing(_:))))
        self.registerForKeyboardNotifications()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func createEventButtonPressed(_ sender: Any) {
        let title = createEventTitleTextField.text!
        let eventDescription = createEventDescriptionTextView.text!
        let eventDate = createEventDateTextField.text!
        let eventTime = createEventTimeTextField.text!
        let eventAddress = createEventAddressTextView.text!
        let eventType = createEventTypeTextField.text!
        let associatedMember = createEventMemberTextField.text!
        let fundGoal = createEventFundGoalTextField.text!
        let eventPicURL = "" //TODO: convert image to bytes
        
        //TODO: date picker, time picker, event type picker, member search selector
        
        //TODO: update Event class with address, time properties
        if (!(createEventTitleTextField.text?.isEmpty)! &&
            !(createEventDescriptionTextView.text?.isEmpty)! &&
            !(createEventDateTextField.text?.isEmpty)! &&
            !(createEventTimeTextField.text?.isEmpty)! &&
            !(createEventAddressTextView.text?.isEmpty)! &&
            !(createEventTypeTextField.text?.isEmpty)! &&
            !(createEventMemberTextField.text?.isEmpty)! &&
            !(createEventFundGoalTextField.text?.isEmpty)! &&
            !(createEventTitleTextField.text?.isEmpty)!) {
            event = Event(title: title, eventDate: eventDate, eventDescription: eventDescription, fundGoal: Double(fundGoal)!, currentFunds: 0.0, eventPicURL: eventPicURL, associatedMember: associatedMember, eventType: eventType)
            createEvent(event: event)
        } else {
            self.displayAlert(alertTitle: "Missing Info", alertMessage: "At least one of the required fields is empty.\nPlease fill out all fields and try again.")
        }
    }
    
    func createEvent(event: Event) {
        let url = URL(string: URL_FOR_CREATE_EVENT)!
        var request = URLRequest(url: url)
        request.httpBody = try? JSONEncoder().encode(event)
        request.httpMethod = "POST"
        
        URLSession.shared.dataTask(with: request) { (data, response, error) -> Void in
            guard let data = data else {
                print(error!.localizedDescription)
                self.displayAlert(alertTitle: "Error Creating Event", alertMessage: "The event could not be created.")
                return
            }
            
            guard let result = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any], let success = result?["success"] as? Bool, success == true else {
                print("Error creating event. Please try again.")
                self.displayAlert(alertTitle: "Error Creating Event", alertMessage: "The event could not be created.")
                return
            }
            
            print("Event created successfully!")
            }.resume()

        self.performSegue(withIdentifier: "ShowEventList", sender: self)
    }
    
    func displayAlert(alertTitle: String, alertMessage: String) {
        let alertController = UIAlertController(title: alertTitle, message:
            alertMessage, preferredStyle: UIAlertControllerStyle.alert)
        alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default,handler: nil))
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    func registerForKeyboardNotifications() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWasShown(_:)), name: Notification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWasHidden(_:)), name: Notification.Name.UIKeyboardWillHide, object: nil)
    }
    
    @objc func keyboardWasShown(_ notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        let contentInsets = UIEdgeInsetsMake(0.0, 0.0, keyboardFrame.height, 0.0)
        scrollView.contentInset = contentInsets
        scrollView.scrollIndicatorInsets = contentInsets
        
        if (activeField != nil) {
            scrollView.scrollRectToVisible(activeField!.frame, animated: true)
        } else if (createEventDescriptionTextView.isFirstResponder) {
            scrollView.scrollRectToVisible(createEventDescriptionTextView.frame, animated: true)
        } else if (createEventAddressTextView.isFirstResponder) {
            scrollView.scrollRectToVisible(createEventAddressTextView.frame, animated: true)
        }
    }
    
    @objc func keyboardWasHidden(_ notification: Notification) {
        let contentInsets = UIEdgeInsets.zero
        scrollView.contentInset = contentInsets
        scrollView.scrollIndicatorInsets = contentInsets
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        activeView = textView
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        activeView = nil
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        activeField = textField
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        activeField = nil
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
