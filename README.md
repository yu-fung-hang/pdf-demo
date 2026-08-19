# PDF API Templates

Sometimes developers are asked to write a program to obtain reports from partners and save them into a storage service like AWS S3 instead of doing it manually. In this project, a sample API demonstrates how to implement this using Java.

## How to run this project
1. Clone this project on `IntelliJ IDEA`;
2. Update your AWS S3 properties in `application.properties`;
3. Run `PdfDemoApplication`;
4. Open Postman. Send a POST request to `http://localhost:8080/pdf/saveFromRemote/s3`. The following is a body example:
```
{
    "url": "https://www.canada.ca/content/dam/ircc/migration/ircc/english/pdf/pub/discover.pdf",
    "name": "canada"
}
```