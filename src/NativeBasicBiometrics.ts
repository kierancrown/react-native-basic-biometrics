import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type { BiometryType } from 'react-native-basic-biometrics';

export interface Spec extends TurboModule {
  canAuthenticate(): Promise<boolean>;
  requestBioAuth(title: string, subtitle: string): Promise<boolean>;
  getBiometryType(): Promise<BiometryType>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('BasicBiometrics');
