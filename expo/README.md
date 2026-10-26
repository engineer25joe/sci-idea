# BiasharaHub KE (Expo + EAS)

This folder contains the Expo version of BiasharaHub. The original Kotlin/Jetpack Compose Android project remains in the parent directory.

## Run locally

From this folder, run `npm install`, then `npm start`.

## Build an Android preview APK with EAS

1. Sign in to an Expo account: `npx eas-cli login`.
2. From this folder, run `npm run build:preview`.

This uses the EAS `preview` profile to create an internally distributed installable APK. On the first build, EAS may ask you to link the project to your Expo account and create Android signing credentials. The equivalent command is `npx eas-cli build --platform android --profile preview`.

The idea cards are a starter catalogue drawn from the Android app. Saved items and calculator inputs currently last for the current app session.
