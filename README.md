Project Title: 
RegIt – QR Code-Based Event Registration and Management System

Group: Hackstreet Boys 

Members:

Jared Sheohn Acebes

James Ewican

Jervin Ryle Milleza

Jamiel Kyne Pinca

Figma: 
https://www.figma.com/design/rp07a9QJX8UqlpdMU4U4TD/Capstone?node-id=0-1&t=Vm6zcFJn4uRQTgu9-1

Class Diagram - Initial:
https://lucid.app/lucidchart/59aadb00-35b2-4540-82e2-3cbe8858e335/edit?invitationId=inv_33673637-acf1-48ba-b3ea-faf7053048f4&fbclid=IwZXh0bgNhZW0CMTEAAR0xfiTdkHPSL44FXmbIo95U2XfGb4g_JW2s4bpA81364KX14_p6TgKNfQU_aem_mtnW283TWKrhedhrg8jfWA&page=0_0#

Project Overview:

RegIt is a comprehensive event registration and management system designed to modernize and simplify the registration process for organizations. By leveraging QR code technology, the system eliminates the need for traditional paper forms, reducing wait times and improving overall event efficiency. RegIt provides both participants and organizers with a seamless, automated registration experience, enhancing event management and data tracking.


How It Works:

Members of the organization create an account through answering the forms, where their basic information is securely stored and converted into a unique QR code. During an event, participants simply scan their QR code at the registration desk, and their details are automatically recorded in the system. This process eliminates manual data entry, significantly speeding up the check-in process.


Key Features:

Instant QR Code Registration - Participants can register for events in seconds by scanning their QR code, eliminating the need for manual forms.

Admin Dashboard for Event Management - Event organizers can create, manage, and monitor multiple events through a centralized dashboard, enabling them to track participant numbers in real-time.

Real-Time Data and Insights - Organizers have access to up-to-date statistics, including the number of attendees, which helps in planning and decision-making.


Benefits:

Streamlined Registration Process - Faster and more efficient check-ins reduce long queues and improve participant satisfaction.

Accurate and Real-Time Reporting - Organizers can access real-time data on participant numbers, allowing for better event management and reporting.

Enhanced User Experience - The elimination of manual forms ensures a smooth registration experience for both attendees and organizers.

Scalable and Flexible - RegIt can handle multiple events simultaneously, making it ideal for organizations of any size.

Objective:

The primary goal of RegIt is to revolutionize event registration processes by integrating QR code technology, ensuring faster, more efficient, and accurate event management while providing valuable insights to organizers. This project aims to demonstrate how technology can improve operational efficiency, reduce administrative burden, and enhance the overall event experience.

Design Patterns Used:

Singleton Pattern
QRCodeGenerator Class: Ensures that only one QR code is generated per user, maintaining uniqueness and preventing duplication.
EventServiceManager Class: Manages all events and attendees using a single shared instance, reducing overhead and ensuring consistent event handling throughout the system.

Builder Pattern
Event Class: Allows flexible creation of Event objects by enabling the omission of optional fields. For example, if the event venue is not finalized, it can be left blank and marked as "To be announced" (TBA), offering flexibility for incomplete information during event planning.

Abstract Factory Pattern
Attendee Class: Supports the creation of two types of attendees: Regular and VIP. This pattern streamlines the registration process by ensuring efficient and consistent attendee creation, allowing for future extensibility and easier management of attendee types.

