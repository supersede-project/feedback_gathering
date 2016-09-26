/**
 * A helper function to realize mixins.
 *
 * @param derivedCtor The class that should get extended.
 * @param baseCtors The array of mixins that should get applied.
 */
export function applyMixins(derivedCtor: any, baseCtors: any[]) {
  baseCtors.forEach(baseCtor => {
    Object.getOwnPropertyNames(baseCtor.prototype).forEach(name => {
      derivedCtor.prototype[name] = baseCtor.prototype[name];
    });
  });
}
