package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;


public class Day2 {

    public static void main(String[] args) {
        Day2 day2 = new Day2();

        System.out.println("SAMPLE 1: " + day2.part1("day2_sample.txt"));
        System.out.println("PART 1: " + day2.part1("day2_input.txt"));

        System.out.println("SAMPLE 2: " + day2.part2("day2_sample.txt"));
        System.out.println("PART 2: " + day2.part2("day2_input.txt"));


    }

    FileManager fileManager = new FileManager();

    private Command parseLine(String line) {
        String[] result = line.split("\\s");
        Command command = new Command();
        command.setDirection(result[0]);
        command.setValue(Integer.parseInt(result[1]));
        return command;
    }

    public Integer part1(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context()) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Command command = parseLine(line);

                if(command.getDirection().equals("forward")) {
                    context.setX(context.getX()+command.getValue());
                } else if(command.getDirection().equals("down")) {
                    context.setZ(context.getZ()+command.getValue());
                } else if(command.getDirection().equals("up")) {
                    context.setZ(context.getZ()-command.getValue());
                }
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);

        return lp.getContext().getX() * lp.getContext().getZ();
    }

    public Integer part2(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context()) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Command command = parseLine(line);

                if(command.getDirection().equals("forward")) {
                    context.setX(context.getX()+command.getValue());
                    context.setZ(context.getZ()+(context.getAim()*command.getValue()));
                } else if(command.getDirection().equals("down")) {
                    //context.setZ(context.getZ()+command.getValue());
                    context.setAim(context.getAim()+command.getValue());
                } else if(command.getDirection().equals("up")) {
                    //context.setZ(context.getZ()-command.getValue());
                    context.setAim(context.getAim()-command.getValue());
                }
                //System.out.println(context);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);

        return lp.getContext().getX() * lp.getContext().getZ();
    }


    private static class Command {
        String direction;
        Integer value;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

    }

    private static class Context {
        Integer z = 0;
        Integer x = 0;
        Integer aim = 0;

        public Integer getZ() {
            return z;
        }

        public void setZ(Integer z) {
            this.z = z;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getAim() {
            return aim;
        }

        public void setAim(Integer aim) {
            this.aim = aim;
        }

        @Override
        public String toString() {
            return "Context{" +
                    "z=" + z +
                    ", x=" + x +
                    ", aim=" + aim +
                    '}';
        }
    }
}
