import { GroupBase } from "react-select";
import AsyncSelect, { AsyncProps } from "react-select/async";

function CustomAsyncSelect<
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>
>(props: AsyncProps<Option, IsMulti, Group>) {
  return (
    <AsyncSelect
      menuPlacement="auto"
      styles={{
        control: (base) => ({
          ...base
        }),
        dropdownIndicator: (base) => ({
          ...base
        }),
        clearIndicator: (base) => ({
          ...base
        }),
        menu: (base) => ({
          ...base
        }),
        menuPortal: (base) => ({ ...base, zIndex: 9999 }),
        valueContainer: (base) => ({
          ...base,
          flexWrap: "nowrap",
          overflow: "hidden",
          paddingTop: 0,
          maxWidth: 300
        }),
        multiValue: (base) => ({
          ...base,
          flexShrink: 0
        })
      }}
      theme={(theme) => ({
        ...theme,
        colors: {
          ...theme.colors,
          primary: "#8b67e0"
        }
      })}
      className={`${props.className ?? ""} caret-ap-lavender-700`}
      menuPortalTarget={document.body}
      {...props}
    />
  );
}

export default CustomAsyncSelect;
