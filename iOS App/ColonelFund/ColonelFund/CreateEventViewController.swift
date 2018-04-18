//
//  CreateEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class CreateEventViewController: UIViewController, UITextViewDelegate, UITextFieldDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, UIPickerViewDelegate, UIPickerViewDataSource {

    //MARK: Properties
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var createEventImage: UIImageView!
    @IBOutlet weak var createEventTitleTextField: UITextField!
    @IBOutlet weak var createEventDateTextField: UITextField!
    @IBOutlet weak var createEventTimeTextField: UITextField!
    @IBOutlet weak var createEventAddressLine1TextField: UITextField!
    @IBOutlet weak var createEventAddressLine2TextField: UITextField!
    @IBOutlet weak var createEventCityTextField: UITextField!
    @IBOutlet weak var createEventStateTextField: UITextField!
    @IBOutlet weak var createEventZipCodeTextField: UITextField!
    @IBOutlet weak var createEventTypeTextField: UITextField!
    @IBOutlet weak var createEventFundGoalTextField: UITextField!
    @IBOutlet weak var createEventDescriptionTextView: UITextView!
    @IBOutlet weak var createEventButton: UIButton!
    
    var event: Event! = nil
    let URL_FOR_CREATE_EVENT = "https://wesll.com/colonelfund/create_event.php"
    var activeView: UITextView?
    var activeField: UITextField?
    var eventPicData = "default"
    let eventTypePicker = UIPickerView()
    let eventTypePickerData = ["BBQ", "Emergency", "Medical", "Party", "Unknown"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        eventTypePicker.delegate = self
        
        createEventTitleTextField.delegate = self
        createEventDateTextField.delegate = self
        createEventTimeTextField.delegate = self
        createEventAddressLine1TextField.delegate = self
        createEventAddressLine2TextField.delegate = self
        createEventCityTextField.delegate = self
        createEventStateTextField.delegate = self
        createEventZipCodeTextField.delegate = self
        createEventTypeTextField.delegate = self
        createEventFundGoalTextField.delegate = self
        createEventDescriptionTextView.delegate = self
        
        createEventDescriptionTextView.text = "Description of event"
        createEventDescriptionTextView.textColor = UIColor(red: 199/255, green: 199/255, blue: 205/255, alpha: 1.0) //to match textField color
        createEventDescriptionTextView.layer.borderColor = UIColor(red: 199/255, green: 199/255, blue: 205/255, alpha: 1.0).cgColor //to match textField color
        createEventDescriptionTextView.layer.borderWidth = 0.5
        createEventDescriptionTextView.layer.cornerRadius = 5.0
        
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self.view, action: #selector(UIView.endEditing(_:))))
        self.registerForKeyboardNotifications()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.view.endEditing(true)
    }
    
    @IBAction func photoButtonPressed(_ sender: Any) {
        let imagePicker = UIImagePickerController()
        imagePicker.delegate = self
        imagePicker.sourceType = UIImagePickerControllerSourceType.photoLibrary
        
        self.present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func createEventButtonPressed(_ sender: Any) {
        let title = createEventTitleTextField.text!
        let eventDescription = createEventDescriptionTextView.text!
        let eventDate = createEventDateTextField.text!
        let eventTime = createEventTimeTextField.text!
        let addressLine1 = createEventAddressLine1TextField.text!
        let addressLine2 = createEventAddressLine2TextField.text!
        let city = createEventCityTextField.text!
        let state = createEventStateTextField.text!
        let zipCode = createEventZipCodeTextField.text!
        let eventType = createEventTypeTextField.text!
        let associatedMember = User.currentUser.getUserID()
        let fundGoal = createEventFundGoalTextField.text!
        
        let eventAddress = Address(addressLine1: addressLine1, addressLine2: addressLine2, city: city, state: state, zipCode: zipCode)
        
        if (!createEventTitleTextField.text!.isEmpty &&
            !createEventDateTextField.text!.isEmpty &&
            !createEventTimeTextField.text!.isEmpty &&
            !createEventAddressLine1TextField.text!.isEmpty &&
            !createEventCityTextField.text!.isEmpty &&
            !createEventStateTextField.text!.isEmpty &&
            !createEventZipCodeTextField.text!.isEmpty &&
            !createEventTypeTextField.text!.isEmpty &&
            !createEventFundGoalTextField.text!.isEmpty &&
            createEventDescriptionTextView.textColor != UIColor(red: 199/255, green: 199/255, blue: 205/255, alpha: 1.0) &&
            !createEventDescriptionTextView.text!.isEmpty) {
            event = Event(title: title, eventDate: eventDate, eventDescription: eventDescription, fundGoal: Double(fundGoal)!, currentFunds: 0.0, eventPicData: eventPicData, associatedMember: associatedMember, eventType: eventType, eventTime: eventTime, address: eventAddress.toString())
            createEvent(event: event)
        } else {
            self.displayAlert(alertTitle: "Missing Info", alertMessage: "At least one of the required fields is empty.\nPlease fill out all fields and try again.", segue: false)
        }
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        createEventImage.image = info[UIImagePickerControllerOriginalImage] as? UIImage
        
        let viewWidth = createEventImage.bounds.width
        let viewHeight = createEventImage.bounds.height
        
        let croppedImage = createEventImage.image!.crop(cropSize: CGSize(width: viewWidth, height: viewHeight))

        let imageData = UIImageJPEGRepresentation(croppedImage, 0.4)
        print("cropped size: \(imageData!.count) bytes")
        print("cropped width: \(croppedImage.size.width) cropped height: \(croppedImage.size.height)")

        
        self.eventPicData = imageData!.base64EncodedString(options: Data.Base64EncodingOptions(rawValue: 0)).addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!.replacingOccurrences(of: "+", with: "%2B")
        createEventImage.image = croppedImage
        self.dismiss(animated: true, completion: nil)
    }
    
    func createEvent(event: Event) {
        let url = URL(string: URL_FOR_CREATE_EVENT)!
        var request = URLRequest(url: url)
        //Commented code can be used instead if server is set to accept json instead of form encoded text
//        request.httpBody = try? JSONEncoder().encode(event)
//        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
//        request.addValue("application/json", forHTTPHeaderField: "Accept")
        request.httpBody = event.toFormEncoded().data(using: .utf8)
        request.httpMethod = "POST"

        
        URLSession.shared.dataTask(with: request) { (data, response, error) -> Void in
            print("response: \(response!)")
            let statusCode = (response as! HTTPURLResponse).statusCode
            
            if (statusCode != 200) {
                if (statusCode == 413) {
                    self.displayAlert(alertTitle: "Error Creating Event", alertMessage: "The image you tried to upload is too large. Please select a smaller image", segue: false)
                } else {
                    self.displayAlert(alertTitle: "Error Creating Event", alertMessage: "The event could not be created due to status code \(statusCode)", segue: false)
                }
                return
            }
            
            guard let data = data else {
                print(error!.localizedDescription)
                self.displayAlert(alertTitle: "Error Creating Event", alertMessage: "The event could not be created. Error with response data from server.", segue: false)
                return
            }
            
            print("data: \(String(data: data, encoding: .utf8)!)")
            
            guard let result = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any], let error = result?["error"] as? Bool, error == false else {
                print("Error creating event. Please try again.")
                self.displayAlert(alertTitle: "Error Creating Event", alertMessage: "The event could not be created. Error from server.", segue: false)
                return
            }
            
            print("Event created successfully!")
            self.displayAlert(alertTitle: "Success", alertMessage: "The event has been successfully created", segue: true)
        }.resume()
    }
    
    func displayAlert(alertTitle: String, alertMessage: String, segue: Bool) {
        let alertController = UIAlertController(title: alertTitle, message:
            alertMessage, preferredStyle: UIAlertControllerStyle.alert)
        
        if segue {
            alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: {action in self.performSegue(withIdentifier: "ShowMainScreen", sender: self)}))
        } else {
            alertController.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        }
        
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
        }
    }
    
    @objc func keyboardWasHidden(_ notification: Notification) {
        let contentInsets = UIEdgeInsets.zero
        scrollView.contentInset = contentInsets
        scrollView.scrollIndicatorInsets = contentInsets
    }
    
    //MARK: Text Field Input Control
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.text!.isEmpty) {
            self.createEventButton.isEnabled = false
        }
        if (textField == createEventFundGoalTextField) {
            if string.isEmpty {
                return true
            } else if (Double(string) != 0) {
                shouldEnableCreateEventButton()
            }
            
            // Build the full current string: TextField right now only contains the
            // previous valid value. Use provided info to build up the new version.
            // Can't just concat the two strings because the user might've moved the
            // cursor and delete something in the middle.
            let currentText = textField.text ?? ""
            let replacementText = (currentText as NSString).replacingCharacters(in: range, with: string)
            
            
            // Use custom string extension to check if the string is valid double with specified amount of decimal places
            return replacementText.isValidDouble(maxDecimalPlaces: 2)
        } else if (textField == createEventTypeTextField) {
            return false
        } else {
            return true
        }
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        activeView = textView
        if (textView.textColor == UIColor(red: 199/255, green: 199/255, blue: 205/255, alpha: 1.0)) {
            textView.text = nil
            textView.textColor = UIColor.black
            self.createEventButton.isEnabled = false
        }
        shouldEnableCreateEventButton()
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        activeView = nil
        if (textView.text.isEmpty) {
            textView.textColor = UIColor(red: 199/255, green: 199/255, blue: 205/255, alpha: 1.0)
            if (textView == createEventDescriptionTextView) {
                textView.text = "Description of event"
                self.createEventButton.isEnabled = false
            }
        }
        shouldEnableCreateEventButton()
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        activeField = textField
        if (textField == self.createEventDateTextField) {
            showDatePicker()
        } else if (textField == self.createEventTimeTextField) {
            showTimePicker()
        } else if (textField == self.createEventTypeTextField) {
            showEventTypePicker()
        }
        shouldEnableCreateEventButton()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        activeField = nil
        shouldEnableCreateEventButton()
    }
    
    func textFieldShouldClear(_ textField: UITextField) -> Bool {
        shouldEnableCreateEventButton()
        return true
    }
    
    func shouldEnableCreateEventButton() {
        if (createEventTitleTextField.text!.isEmpty ||
            createEventDateTextField.text!.isEmpty ||
            createEventTimeTextField.text!.isEmpty ||
            createEventAddressLine1TextField.text!.isEmpty ||
            createEventAddressLine2TextField.text!.isEmpty ||
            createEventCityTextField.text!.isEmpty ||
            createEventStateTextField.text!.isEmpty ||
            createEventZipCodeTextField.text!.isEmpty ||
            createEventTypeTextField.text!.isEmpty ||
            createEventFundGoalTextField.text!.isEmpty ||
            createEventDescriptionTextView.textColor == UIColor(red: 199/255, green: 199/255, blue: 205/255, alpha: 1.0) ||
            createEventDescriptionTextView.text!.isEmpty) {
            self.createEventButton.isEnabled = false
        } else {
            self.createEventButton.isEnabled = true
        }
    }
    
    func showDatePicker() {
        let datePicker = UIDatePicker()
        datePicker.datePickerMode = UIDatePickerMode.date
        createEventDateTextField.inputView = datePicker
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        createEventDateTextField.text = dateFormatter.string(from: Date())
        datePicker.addTarget(self, action: #selector(datePickerValueChanged(_:)), for: UIControlEvents.valueChanged)
    }
    
    @objc func datePickerValueChanged(_ sender: UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"

        createEventDateTextField.text = dateFormatter.string(from: sender.date)
    }
    
    func showTimePicker() {
        let timePicker = UIDatePicker()
        timePicker.datePickerMode = UIDatePickerMode.time
        createEventTimeTextField.inputView = timePicker
        let timeFormatter = DateFormatter()
        timeFormatter.timeStyle = .short
        
        createEventTimeTextField.text = timeFormatter.string(from: Date())
        timePicker.addTarget(self, action: #selector(timePickerValueChanged(_:)), for: UIControlEvents.valueChanged)
    }
    
    @objc func timePickerValueChanged(_ sender: UIDatePicker) {
        let timeFormatter = DateFormatter()
        timeFormatter.timeStyle = .short

        createEventTimeTextField.text = timeFormatter.string(from: sender.date)
    }
    
    func showEventTypePicker() {
        createEventTypeTextField.inputView = eventTypePicker
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return eventTypePickerData.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return eventTypePickerData[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        self.createEventTypeTextField.text = eventTypePickerData[row]
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

extension UIImage {
    func crop(cropSize: CGSize) -> UIImage {
        guard let cgimage = self.cgImage else {
            return self
        }
        
        let contextImage: UIImage = UIImage(cgImage: cgimage)
        let contextSize: CGSize = contextImage.size
        
        var posX: CGFloat = 0.0
        var posY: CGFloat = 0.0
        let cropAspect: CGFloat = cropSize.width / cropSize.height
        
        var cropWidth: CGFloat = cropSize.width
        var cropHeight: CGFloat = cropSize.height
        
        if (cropSize.width > cropSize.height) { //Landscape image
            cropWidth = contextSize.width
            cropHeight = contextSize.width / cropAspect
            posY = (contextSize.height - cropHeight) / 2
        } else if (cropSize.width < cropSize.height) { //Portrait image
            cropHeight = contextSize.height
            cropWidth = contextSize.height * cropAspect
            posX = (contextSize.width - cropWidth) / 2
        } else { //Square image
            if (contextSize.width >= contextSize.height) { //Square on landscape (or square)
                cropHeight = contextSize.height
                cropWidth = contextSize.height * cropAspect
                posX = (contextSize.width - cropWidth) / 2
            } else { //Square on portrait
                cropWidth = contextSize.width
                cropHeight = contextSize.width / cropAspect
                posY = (contextSize.height - cropHeight) / 2
            }
        }
        
        let rect: CGRect = CGRect(x: posX, y: posY, width: cropWidth, height: cropHeight)
        
        // Create bitmap image from context using rect
        let imageRef: CGImage = contextImage.cgImage!.cropping(to: rect)!
        
        // Create a new image based on the imageRef and rotate back to the original orientation
        let cropped: UIImage = UIImage(cgImage: imageRef, scale: self.scale, orientation: self.imageOrientation)
        
        cropped.draw(in: CGRect(x : 0, y : 0, width : cropSize.width, height : cropSize.height))
        
        return cropped
    }
}
