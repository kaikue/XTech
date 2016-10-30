package kaikue.xtech.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockSplitter extends BlockMirror {

	public BlockSplitter() {
		super("splitter", "Splits beams in two", Material.GLASS, 0.3f, SoundType.GLASS);
	}

}
