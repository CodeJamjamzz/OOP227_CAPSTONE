# Project Title: RegIt – QR Code-Based Event Registration and Management System

## Important Reminder: 
This project requires a **stable internet connection** to operate properly. Ensure that your device is connected to a reliable network before running the application.


### **Group:** Hackstreet Boys

### Members:
- Jared Sheohn Acebes
- James Ewican
- Jervin Ryle Milleza
- Jamiel Kyne Pinca

---

### Figma Design:
[View Figma Design](https://www.figma.com/design/rp07a9QJX8UqlpdMU4U4TD/Capstone?node-id=0-1&t=Vm6zcFJn4uRQTgu9-1)

### Class Diagram:
[View Class Diagram Part-1](https://lucid.app/lucidchart/59aadb00-35b2-4540-82e2-3cbe8858e335/edit?invitationId=inv_33673637-acf1-48ba-b3ea-faf7053048f4&fbclid=IwZXh0bgNhZW0CMTEAAR0xfiTdkHPSL44FXmbIo95U2XfGb4g_JW2s4bpA81364KX14_p6TgKNfQU_aem_mtnW283TWKrhedhrg8jfWA&page=0_0#)

[View Class Diagram Part-2](https://lucid.app/lucidchart/00319354-0ff8-4284-bd4d-7faafdb14f94/edit?viewport_loc=-1012%2C-705%2C4573%2C2157%2C0_0&invitationId=inv_a22dffd6-9a8e-40bc-b753-1dd8d51b2708)

---

## Project Overview:

**RegIt** is a comprehensive event registration and management system designed to modernize and simplify the registration process for organizations. By leveraging QR code technology, the system eliminates the need for traditional paper forms, reducing wait times and improving overall event efficiency. RegIt provides both participants and organizers with a seamless, automated registration experience, enhancing event management and data tracking.

---
## Design Patterns Used:

### 1. **Singleton Creational Design Pattern**
   - **QRCodeGenerator Class:** Ensures that only one QR code is generated per user, maintaining uniqueness and preventing duplication.
   - **EventServiceManager Class:** Manages all events and attendees using a single shared instance, reducing overhead and ensuring consistent event handling throughout the system.
   - **RegItFirebaseController:** Same with the EventServiceManager this class only needs one instance inorder to connect the project to the database 

### 2. **Builder Creational Design Pattern**
   - **Event Class:** Allows flexible creation of Event objects by enabling the omission of optional fields. For example, if the event venue is not finalized, it can be left blank and marked as "To be announced" (TBA), offering flexibility for incomplete information during event planning.

### 3. **Adaptor Structural Design Pattern**
   - **RecyclerView.Adapter:** It sets up the raw data from the events and attendees in a method so that the RecyclerView can properly display the objects in their respective layouts

---
## How It Works:

1. **Account Creation:**  
   Members of the organization create an account by answering a series of forms, where their basic information is securely stored and converted into a unique QR code.

2. **QR Code Check-In:**  
   During the event, participants simply scan their QR code at the registration desk. Their details are automatically recorded in the system, eliminating manual data entry and speeding up the check-in process.

---

## Key Features:

- **Instant QR Code Registration**  
   Participants can register for events in seconds by scanning their QR code, eliminating the need for manual forms.

- **Admin Dashboard for Event Management**  
   Event organizers can create, manage, and monitor multiple events through a centralized dashboard, enabling them to track participant numbers in real-time.

- **Real-Time Data and Insights**  
   Organizers have access to up-to-date statistics, including the number of attendees, which helps in planning and decision-making.

---

## Benefits:

- **Streamlined Registration Process**  
   Faster and more efficient check-ins reduce long queues and improve participant satisfaction.

- **Accurate and Real-Time Reporting**  
   Organizers can access real-time data on participant numbers, allowing for better event management and reporting.

- **Enhanced User Experience**  
   The elimination of manual forms ensures a smooth registration experience for both attendees and organizers.

- **Scalable and Flexible**  
   RegIt can handle multiple events simultaneously, making it ideal for organizations of any size.

---

## Objective:

The primary goal of **RegIt** is to revolutionize event registration processes by integrating QR code technology, ensuring faster, more efficient, and accurate event management while providing valuable insights to organizers. This project aims to demonstrate how technology can improve operational efficiency, reduce administrative burden, and enhance the overall event experience.

