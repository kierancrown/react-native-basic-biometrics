import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-basic-biometrics' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const BasicBiometricsModule = isTurboModuleEnabled
  ? require('./NativeBasicBiometrics').default
  : NativeModules.BasicBiometrics;

const BasicBiometrics = BasicBiometricsModule
  ? BasicBiometricsModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

// Define canAuthenticate method
export function canAuthenticate(): Promise<boolean> {
  return BasicBiometrics.canAuthenticate();
}

// Define requestBioAuth method
export function requestBioAuth(
  title: string,
  subtitle: string
): Promise<boolean> {
  return BasicBiometrics.requestBioAuth(title, subtitle);
}

// Define getBiometryType method
export function getBiometryType(): Promise<BiometryType> {
  return BasicBiometrics.getBiometryType();
}

export type BiometryType =
  | 'TouchID'
  | 'FaceID'
  | 'Fingerprint'
  | 'Face'
  | 'Unknown'
  | 'None';
