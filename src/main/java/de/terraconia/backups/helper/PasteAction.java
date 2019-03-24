package de.terraconia.backups.helper;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.terraconia.backups.tasks.SchematicTask;
import org.bukkit.Bukkit;
import org.primesoft.asyncworldedit.api.utils.IFuncParamEx;
import org.primesoft.asyncworldedit.api.worldedit.ICancelabeEditSession;

public class PasteAction implements IFuncParamEx<Integer, ICancelabeEditSession, MaxChangedBlocksException> {
    private BlockVector3 origin;
    private ClipboardHolder holder;
    private boolean air;

    public PasteAction(BlockVector3 origin, ClipboardHolder holder, boolean air){
        this.holder = holder;
        this.air = air;
        this.origin = origin;
    }

    public PasteAction(SchematicTask task) {
        this.origin = task.getOrigin();
        this.holder = new ClipboardHolder(task.getClipboard());
        this.air = task.isIgnoreAir();
    }

    @Override
    public Integer execute(ICancelabeEditSession editSession) throws MaxChangedBlocksException {
        editSession.enableQueue();

        final Operation operation = this.holder
            .createPaste(editSession)
            .to(this.origin)
            .ignoreAirBlocks(this.air)
            .build();
        Operations.completeBlindly(operation);
        editSession.flushSession();

        return editSession.getBlockChangeCount();
        }
}
