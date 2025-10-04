<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="refresh" content="5;url=${redirectUrl}">
    <title>${eventTitle}</title>
    <!-- Open Graph meta tags -->
    <meta property="og:title" content="${listingTitle}">
    <meta property="og:site_name" content="${siteName}">
    <meta property="og:description" content="${listingDescription}">
    <meta property="og:image" content="${listingImageUrl}">
    <meta property="og:url" content="${listingUrl}">
    <meta property="og:type" content="website">

    <meta name="twitter:card" content="summary" />
    <meta name="twitter:image" content="${listingImageUrl}" />
    <meta name="twitter:title" content="${listingTitle}">
    <meta name="twitter:description" content="${listingDescription}">

    <script>
        window.location.href = "${redirectUrl}";

        setTimeout(function () {
            window.location.href = "${storeUrl}";
        }, 3000); // Задержка для попытки открыть приложение
    </script>
</head>
<body>
    <p>Redirecting to the app...</p>
</body>
</html>