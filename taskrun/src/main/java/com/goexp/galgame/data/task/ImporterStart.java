package com.goexp.galgame.data.task;

import com.goexp.galgame.data.task.mapreduce.GetTrueCVTask;
import com.goexp.galgame.data.task.mapreduce.MarkSameGameTask;
import com.goexp.galgame.data.task.others.GroupBrandTask;
import com.goexp.galgame.data.task.others.ImportFromLocalAliveBrandTask;
import com.goexp.galgame.data.task.others.ImportOnceTagTask;
import com.goexp.galgame.data.task.others.UpdateBrandTask;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.System.out;

public class ImporterStart {

    private static class Functions {

        private final List<Peer> funcTable;

        private Functions() {
            funcTable = List.of(
                    new Peer("upgrade-game-date", "Upgrade game info from date to another", FromDateRangeTask::main),
                    new Peer("upgrade-game-brand", "Upgrade game info brand by brand", FromAliveBrandTask::main),
                    new Peer("upgrade-brand", "Upgrade brand info ", UpdateBrandTask::main),
                    new Peer("get-truecv", "Get real CV name", GetTrueCVTask::main),
                    new Peer("group-brand", "Group brand", GroupBrandTask::main),
                    new Peer("mark-same", "Mark the same game", MarkSameGameTask::main),
                    new Peer("import-tag", "Import tag info once", ImportOnceTagTask::main),
                    new Peer("import-game-local", "Import from local cache once", ImportFromLocalAliveBrandTask::main)
            );
        }

        private void showHowToUse() {
            funcTable.forEach(peer -> out.printf("%s:%s\n", peer.name, peer.desc));
        }

        private void exec(String[] args) {
            Objects.requireNonNull(args);

            funcTable.stream()
                    .filter(peer -> args[0].equals(peer.name))
                    .findFirst()
                    .ifPresentOrElse(peer -> peer.run.accept(args)
                            , this::showHowToUse);
        }


        private static class Peer {
            private String name;
            private String desc;
            private Consumer<String[]> run;

            private Peer(String name, String desc, Consumer<String[]> run) {
                this.name = name;
                this.desc = desc;
                this.run = run;
            }
        }
    }


    public static void main(String[] args) {

        final var func = new Functions();

        if (args.length == 0) {
            func.showHowToUse();
        } else {
            func.exec(args);
        }

    }
}
