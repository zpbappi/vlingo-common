// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.common.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SemanticVersionTest {

  @Test
  public void testThatVersionEncodesDecodes() {
    final int semanticVersion_0_0_0 = SemanticVersion.toValue(0, 0, 0);
    assertEquals("0.0.0", SemanticVersion.toString(semanticVersion_0_0_0));

    final int semanticVersion_1_0_0 = SemanticVersion.toValue(1, 0, 0);
    assertEquals("1.0.0", SemanticVersion.toString(semanticVersion_1_0_0));

    final int semanticVersion_0_1_0 = SemanticVersion.toValue(0, 1, 0);
    assertEquals("0.1.0", SemanticVersion.toString(semanticVersion_0_1_0));

    final int semanticVersion_0_0_1 = SemanticVersion.toValue(0, 0, 1);
    assertEquals("0.0.1", SemanticVersion.toString(semanticVersion_0_0_1));

    final int semanticVersion_1_1_0 = SemanticVersion.toValue(1, 1, 0);
    assertEquals("1.1.0", SemanticVersion.toString(semanticVersion_1_1_0));

    final int semanticVersion_1_1_1 = SemanticVersion.toValue(1, 1, 1);
    assertEquals("1.1.1", SemanticVersion.toString(semanticVersion_1_1_1));

    final int semanticVersion_0_1_2 = SemanticVersion.toValue(0, 1, 2);
    assertEquals("0.1.2", SemanticVersion.toString(semanticVersion_0_1_2));

    final int semanticVersion_1_2_3 = SemanticVersion.toValue(1, 2, 3);
    assertEquals("1.2.3", SemanticVersion.toString(semanticVersion_1_2_3));

    final int semanticVersion_129_64_55 = SemanticVersion.toValue(129, 64, 55);
    assertEquals("129.64.55", SemanticVersion.toString(semanticVersion_129_64_55));

    final int semanticVersion_32761_127_127 = SemanticVersion.toValue(32761, 127, 127);
    assertEquals("32761.127.127", SemanticVersion.toString(semanticVersion_32761_127_127));

    final int semanticVersion_32767_255_255 = SemanticVersion.toValue(32767, 255, 255);
    assertEquals("32767.255.255", SemanticVersion.toString(semanticVersion_32767_255_255));
  }

  @Test
  public void testThatStringToValueParses() {
    final int semanticVersion_1_0_0 = SemanticVersion.toValue(1, 0, 0);
    final int semanticVersion_1_0_0_again = SemanticVersion.toValue(SemanticVersion.toString(semanticVersion_1_0_0));
    assertEquals(semanticVersion_1_0_0, semanticVersion_1_0_0_again);

    final int semanticVersion_0_1_0 = SemanticVersion.toValue(0, 1, 0);
    final int semanticVersion_0_1_0_again = SemanticVersion.toValue(SemanticVersion.toString(semanticVersion_0_1_0));
    assertEquals(semanticVersion_0_1_0, semanticVersion_0_1_0_again);

    final int semanticVersion_0_0_1 = SemanticVersion.toValue(0, 0, 1);
    final int semanticVersion_0_0_1_again = SemanticVersion.toValue(SemanticVersion.toString(semanticVersion_0_0_1));
    assertEquals(semanticVersion_0_0_1, semanticVersion_0_0_1_again);

    final int semanticVersion_1_1_0 = SemanticVersion.toValue(1, 1, 0);
    final int semanticVersion_1_1_0_again = SemanticVersion.toValue(SemanticVersion.toString(semanticVersion_1_1_0));
    assertEquals(semanticVersion_1_1_0, semanticVersion_1_1_0_again);

    final int semanticVersion_1_1_1 = SemanticVersion.toValue(1, 1, 1);
    final int semanticVersion_1_1_1_again = SemanticVersion.toValue(SemanticVersion.toString(semanticVersion_1_1_1));
    assertEquals(semanticVersion_1_1_1, semanticVersion_1_1_1_again);
  }

  @Test
  public void testVersionCompatibility() {
    final SemanticVersion version = SemanticVersion.from(1, 0, 0);

    final SemanticVersion majorBump = SemanticVersion.from(2, 0, 0);
    assertTrue(majorBump.isCompatibleWith(version));

    final SemanticVersion minorBump = SemanticVersion.from(1, 1, 0);
    assertTrue(minorBump.isCompatibleWith(version));

    final SemanticVersion patchBump = SemanticVersion.from(1, 0, 1);
    assertTrue(patchBump.isCompatibleWith(version));
  }

  @Test
  public void testVersionIncompatibility() {
    final SemanticVersion version = SemanticVersion.from(1, 0, 0);

    final SemanticVersion majorTooHigh = SemanticVersion.from(3, 0, 0);
    assertFalse(majorTooHigh.isCompatibleWith(version));

    final SemanticVersion majorBumpMinorTooHigh = SemanticVersion.from(2, 1, 0);
    assertFalse(majorBumpMinorTooHigh.isCompatibleWith(version));

    final SemanticVersion minorTooHigh = SemanticVersion.from(1, 2, 0);
    assertFalse(minorTooHigh.isCompatibleWith(version));

    final SemanticVersion minorBumpPatchTooHigh = SemanticVersion.from(1, 1, 1);
    assertFalse(minorBumpPatchTooHigh.isCompatibleWith(version));

    final SemanticVersion patchTooHigh = SemanticVersion.from(1, 0, 2);
    assertFalse(patchTooHigh.isCompatibleWith(version));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testThatMajorVersionMinBoundsCheck() {
    SemanticVersion.toValue(-1, 1, 1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testThatMajorVersionMaxBoundsCheck() {
    SemanticVersion.toValue(32768, 1, 1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testThatMinorVersionMinBoundsCheck() {
    SemanticVersion.toValue(1, -1, 1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testThatMinorVersionMaxBoundsCheck() {
    SemanticVersion.toValue(1, 256, 1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testThatPatchVersionMinBoundsCheck() {
    SemanticVersion.toValue(1, 1, -1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testThatPatchVersionMaxBoundsCheck() {
    SemanticVersion.toValue(1, 1, 256);
  }
}
