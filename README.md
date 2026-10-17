# ISTA Eat (online version)

Students order food from their phones. The seller sees every order live and updates its status
(Reçue, En préparation, Prête, Récupérée). Orders are stored in Appwrite.

Everything lives in ONE file: `index.html`. Edit only that file.

## 1. Appwrite setup (cloud.appwrite.io)
1. Create a project (you can sign in with GitHub).
2. Overview > Add platform > Web. Hostname = where your site will be, without https://
   (GitHub Pages: `YOUR-USERNAME.github.io`).
3. Databases > Create database. Inside it, create a collection named `orders`.
4. In `orders` > Attributes, create (all required, except where noted):
   - `oid`      String, size 20
   - `code`     String, size 10
   - `name`     String, size 100
   - `slot`     String, size 40
   - `status`   String, size 30
   - `payment`  String, size 40 (not required)
   - `created`  String, size 40
   - `total`    Float
   - `items`    String, size 5000
5. In `orders` > Settings:
   - Turn ON "Document security".
   - Permissions: add role "Any" with **Create** only.
6. Auth > Users > Create user: the seller's email + password (this is the Admin login in the app).
7. Back in `orders` > Settings > Permissions: add that user (Users tab, pick the seller)
   with **Read** and **Update**.
8. Copy `endpoint`, `Project ID`, `Database ID` into the APPWRITE block at the top of the
   script in `index.html`. Keep `collection: 'orders'`.

## 2. Put the website online (GitHub Pages)
Upload all files to a GitHub repository (keep the `.github` folder), then
Settings > Pages > Branch `main`, folder `/ (root)` > Save.
Site: https://YOUR-USERNAME.github.io/REPOSITORY-NAME/
(Appwrite Sites can also deploy this repo; then use that domain as the platform hostname in step 1.2.)

## 3. Android app (optional)
Actions tab > "Build ISTA Eat APK" > Run workflow > download `ISTAEat-debug-APK`.
The APK opens your GitHub Pages website, so you only update `index.html` and students get the changes.

## Notes
- Edit the `products` list (dishes, prices) and `pickupSlots` in `index.html`.
- Orders are readable by anyone who knows the API address, so ask students for a first name only.
- If the APPWRITE block still says YOUR_..., the app runs in local demo mode.
