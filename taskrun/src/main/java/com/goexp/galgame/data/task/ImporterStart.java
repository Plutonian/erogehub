package com.goexp.galgame.data.task;

import com.goexp.galgame.data.task.mapreduce.GetTrueCVTask;
import com.goexp.galgame.data.task.others.GroupBrandTask;
import com.goexp.galgame.data.task.others.ImportFromLocalAliveBrandTask;
import com.goexp.galgame.data.task.others.ImportOnceTagTask;
import com.goexp.galgame.data.task.others.UpdateBrandTask;

import java.util.Map;

public class ImporterStart {


    private static void howToUse() {
        var map = Map.of("get-truecv", "Get real CV name",
                "upgrade-game-date", "Upgrade game info from date to another",
                "upgrade-game-brand", "Upgrade game info brand by brand",
                "upgrade-brand", "Upgrade brand info ",
                "import-tag", "Import tag info once",
                "import-game-local", "Import from local cache once",
                "group-brand", "Group brand"
        );


        map.forEach((key, value) -> System.out.printf("%s:%s\n", key, value));


    }

    public static void main(String[] args) {
        if (args.length == 0) {
            howToUse();
        } else {
            switch (args[0]) {
                case "get-truecv": {
                    GetTrueCVTask.main(args);
                    break;
                }
                case "upgrade-game-date": {
                    FromDateRangeTask.main(args);
                    break;
                }
                case "upgrade-game-brand": {
                    FromAliveBrandTask.main(args);
                    break;
                }
                case "upgrade-brand": {
                    UpdateBrandTask.main(args);
                    break;
                }
                case "import-tag": {
                    ImportOnceTagTask.main(args);
                    break;
                }
                case "import-game-local": {
                    ImportFromLocalAliveBrandTask.main(args);
                    break;
                }
                case "group-brand": {
                    GroupBrandTask.main(args);
                    break;
                }
                default: {
                    howToUse();
                    break;
                }

            }
        }

    }
}
