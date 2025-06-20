# External API (Zenquotes)
## Description
>ZenQuotes.io API calls are designed to be easy to use and understand. The result data is formatted as a JSON array. 
Pre-formatted HTML output is available in addition to individual plain text values."

**The basic elements of an API call are as follows:**

https://zenquotes.io/api/[mode]/[key]?option1=value&option2=value

## Requesting a random quote

- **Endpoint:** `GET /api/random`
- **Description:** Returns a random quote.
- **Parameters:** `empty`.
- **Response:**
    - **Status 200**
      - q = quote text 
      - a = author name 
      - h = pre-formatted HTML quote

    ```json
    [
      {
        "q": "To succeed takes more than the desire to win. It also takes the acceptance that we could fail.",
        "a": "Simon Sinek",
        "h": "<blockquote>&ldquo;To succeed takes more than the desire to win. It also takes the acceptance that we could fail.&rdquo; &mdash; <footer>Simon Sinek</footer></blockquote>"
      }
    ]
    ```

## API Usage Limits and Attribution

>Requests are restricted by IP to 5 per 30 second period by default. 
An API key or registered IP is required for unlimited access and to enable Access-Control-Allow-Origin headers. 
We require that you show attribution with a link back to https://zenquotes.io/ when using the free version of this API.

```html
Inspirational quotes provided by <a href="https://zenquotes.io/" target="_blank">ZenQuotes API</a>
```